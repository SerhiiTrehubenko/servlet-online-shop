package com.tsa.shop.application;

import com.tsa.shop.database.implementation.*;
import com.tsa.shop.database.interfaces.*;
import com.tsa.shop.domain.User;
import com.tsa.shop.flyway.DefaultFlywayBridge;
import com.tsa.shop.flyway.FlywayBridge;
import com.tsa.shop.domain.Property;
import com.tsa.shop.argsparser.impl.DefaultArgsParser;
import com.tsa.shop.argsparser.impl.DefaultEnvironmentVariablesContext;
import com.tsa.shop.argsparser.impl.DefaultPropertyReader;
import com.tsa.shop.argsparser.interfaces.ArgsParser;
import com.tsa.shop.argsparser.interfaces.EnvironmentVariablesContext;
import com.tsa.shop.argsparser.interfaces.PropertyReader;
import com.tsa.shop.login.impl.*;
import com.tsa.shop.domain.ProductMapper;
import com.tsa.shop.domain.DtoExtractor;
import com.tsa.shop.domain.ProductService;
import com.tsa.shop.login.repoimpl.DefaultTokenRepository;
import com.tsa.shop.login.repoimpl.UserRepositoryImpl;
import com.tsa.shop.web.WebRequestHandler;
import com.tsa.shop.web.impl.*;
import com.tsa.shop.web.interfaces.*;
import com.tsa.shop.web.servlet.*;
import com.tsa.shop.service.ProductDtoExtractor;
import com.tsa.shop.service.DefaultProductMapper;
import com.tsa.shop.logging.DomainLogger;
import com.tsa.shop.logging.DomainLoggerImpl;
import com.tsa.shop.login.interfaces.LogInFacade;
import com.tsa.shop.login.interfaces.LogInFactory;
import com.tsa.shop.login.repo.TokenRepository;
import com.tsa.shop.login.repo.UserRepository;
import com.tsa.shop.logmessagegenerator.ExceptionInfoExtractor;
import com.tsa.shop.logmessagegenerator.ExceptionInfoExtractorImpl;
import com.tsa.shop.logmessagegenerator.LogMessageGenerator;
import com.tsa.shop.logmessagegenerator.LogMessageGeneratorImpl;
import com.tsa.shop.service.DefaultProductService;
import com.tsa.shop.domain.UriPageConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Credential;

import java.util.UUID;

public class ServletStarter {

    public static void main(String[] args) throws Exception {
        UriCache cache = new UriCache().setUp();

//        Parse Args
        EnvironmentVariablesContext context = new DefaultEnvironmentVariablesContext(Property.PORT, Property.FILE_PROPERTY);
        ArgsParser argsParser = new DefaultArgsParser(context);
        argsParser.parse(args);
        PropertyReader propertyReader = new DefaultPropertyReader(context);

//        Db connection
        DbConnector dbConnector = new DefaultDbConnector(propertyReader);

//        flyway
        FlywayBridge flywayBridge = new DefaultFlywayBridge(propertyReader);
        flywayBridge.migrate();

//        Utility Classes
        ServletRequestParser servletRequestParser = new DefaultServletRequestParser(cache);
        PageGenerator pageGenerator = new DefaultPageGenerator();
        ResponseWriter responseWriter = new DefaultResponseWriter();
        ContentFileProvider contentFileProvider = new DefaultContentFileProvider();
        Response response = new DefaultResponse();

//        NEW DAO Product
        ProductRowFetcher productRowFetcher = new DefaultProductRowFetcher();
        PSResolver psResolver = new DefaultPSResolver();
        ProductDao productDao = new DefaultProductDao(dbConnector, psResolver, productRowFetcher);
        DtoExtractor dtoExtractor = new ProductDtoExtractor();
        ProductMapper productMapper = new DefaultProductMapper();
        ProductService productService = new DefaultProductService(productDao, productMapper, dtoExtractor);

//        Logging
        DomainLogger domainLogger = new DomainLoggerImpl();
        ExceptionInfoExtractor exceptionInfoExtractor = new ExceptionInfoExtractorImpl();
        LogMessageGenerator logMessageGenerator = new LogMessageGeneratorImpl(exceptionInfoExtractor);

//        Authentication
        LogInFactory logInFactory = new LogInFactoryImpl();
        UserRepository userRepository = new UserRepositoryImpl();
        initializeUsers(userRepository);
        TokenRepository tokenRepository = new DefaultTokenRepository();
        LogInFacade logInFacade = new LogInFacadeImpl(logInFactory, userRepository, tokenRepository);

//        Servlets
        WebRequestHandler homeRequestHandler = new HomeWebRequestHandler(servletRequestParser, pageGenerator, responseWriter, response, domainLogger, logMessageGenerator, contentFileProvider);

        WebRequestHandler productFindAllRequestHandler = new ProductFindAllWebRequestHandler(servletRequestParser, pageGenerator, responseWriter, response, domainLogger, logMessageGenerator, productService);
        WebRequestHandler productDeleteRequestHandler = new ProductDeleteWebRequestHandler(servletRequestParser, pageGenerator, responseWriter, response, domainLogger, logMessageGenerator, productService);
        WebRequestHandler productUpdateRequestHandler = new ProductUpdateWebRequestHandler(servletRequestParser, pageGenerator, responseWriter, response, domainLogger, logMessageGenerator, productService);
        WebRequestHandler productAddRequestHandler = new ProductAddWebRequestHandler(servletRequestParser, pageGenerator, responseWriter, response, domainLogger, logMessageGenerator, productService);
        WebRequestHandler pageNotFoundRequestHandler = new PageNotFoundHandler(servletRequestParser, pageGenerator, responseWriter, response, domainLogger, logMessageGenerator);

//        LogIn Servlets
        WebRequestHandler logInWebRequestHandler = new LogInWebRequestHandler(servletRequestParser, pageGenerator, responseWriter, response, domainLogger, logMessageGenerator, logInFacade);
        WebRequestHandler logInDecoratorWebRequestHandler = new LogInDecoratorWebRequestHandler(servletRequestParser, pageGenerator, responseWriter, response, domainLogger, logMessageGenerator, tokenRepository, productAddRequestHandler);

//        Set Servlets
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        String homeUri = UriPageConnector.HOME.getUri();
        servletContextHandler.addServlet(new ServletHolder(homeRequestHandler), homeUri);
        String fileUri = UriPageConnector.SLASH.getUri();
        servletContextHandler.addServlet(new ServletHolder(homeRequestHandler), fileUri);

        String allProducts = UriPageConnector.PRODUCTS.getUri();
        servletContextHandler.addServlet(new ServletHolder(productFindAllRequestHandler), allProducts);

        String deleteProduct = UriPageConnector.PRODUCTS_DELETE.getUri();
        servletContextHandler.addServlet(new ServletHolder(productDeleteRequestHandler), deleteProduct);

        String updateProductGet = UriPageConnector.PRODUCTS_UPDATE.getUri();
        String updateProductPost = UriPageConnector.PRODUCTS_POST_UPDATE.getUri();
        servletContextHandler.addServlet(new ServletHolder(productUpdateRequestHandler), updateProductGet);
        servletContextHandler.addServlet(new ServletHolder(productUpdateRequestHandler), updateProductPost);

        String addProductGet = UriPageConnector.PRODUCTS_ADD.getUri();
        servletContextHandler.addServlet(new ServletHolder(logInDecoratorWebRequestHandler), addProductGet);

        String notFoundUri = UriPageConnector.PRODUCTS.getUri() + "/*";
        servletContextHandler.addServlet(new ServletHolder(pageNotFoundRequestHandler), notFoundUri);

        String logInUri = UriPageConnector.LOG_IN_PAGE.getUri();
        servletContextHandler.addServlet(new ServletHolder(logInWebRequestHandler), logInUri);

//        Start Application
        Server server = new Server(propertyReader.getPort());
        server.setHandler(servletContextHandler);

        server.start();
    }

    private static void initializeUsers(UserRepository userRepository) {
        String email = "tsa@gmail.com";
        String password = "password123456";
        UUID sole = UUID.randomUUID();
        String passwordPlusSoleHash = Credential.MD5.getCredential(password + sole).toString();
        User user = new User(email, passwordPlusSoleHash, sole.toString());
        userRepository.addUser(user);
    }
}
