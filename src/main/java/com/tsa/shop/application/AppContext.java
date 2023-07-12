package com.tsa.shop.application;

import com.tsa.shop.argsparser.impl.DefaultArgsParser;
import com.tsa.shop.argsparser.impl.DefaultEnvironmentVariablesContext;
import com.tsa.shop.argsparser.impl.DefaultPropertyReader;
import com.tsa.shop.argsparser.interfaces.ArgsParser;
import com.tsa.shop.argsparser.interfaces.EnvironmentVariablesContext;
import com.tsa.shop.argsparser.interfaces.PropertyReader;
import com.tsa.shop.cart.CartRepository;
import com.tsa.shop.cart.CartService;
import com.tsa.shop.database.BasicDataSourceAdapter;
import com.tsa.shop.database.interfaces.DbConnector;
import com.tsa.shop.database.interfaces.PSResolver;
import com.tsa.shop.database.interfaces.ProductDao;
import com.tsa.shop.database.interfaces.ProductRowFetcher;
import com.tsa.shop.database.jdbc.DefaultPSResolver;
import com.tsa.shop.database.jdbc.DefaultProductRowFetcher;
import com.tsa.shop.database.jdbc.JdbcProductDao;
import com.tsa.shop.domain.*;
import com.tsa.shop.flyway.DefaultFlywayBridge;
import com.tsa.shop.flyway.FlywayBridge;
import com.tsa.shop.logging.DomainLogger;
import com.tsa.shop.logging.DomainLoggerImpl;
import com.tsa.shop.login.impl.LogInFacadeImpl;
import com.tsa.shop.login.impl.LogInFactoryImpl;
import com.tsa.shop.login.interfaces.LogInFacade;
import com.tsa.shop.login.interfaces.LogInFactory;
import com.tsa.shop.login.repo.TokenRepository;
import com.tsa.shop.login.repo.UserRepository;
import com.tsa.shop.login.repoimpl.DefaultTokenRepository;
import com.tsa.shop.login.repoimpl.UserRepositoryImpl;
import com.tsa.shop.logmessagegenerator.ExceptionInfoExtractor;
import com.tsa.shop.logmessagegenerator.ExceptionInfoExtractorImpl;
import com.tsa.shop.logmessagegenerator.LogMessageGenerator;
import com.tsa.shop.logmessagegenerator.LogMessageGeneratorImpl;
import com.tsa.shop.logout.DefaultLogoutService;
import com.tsa.shop.logout.LogoutService;
import com.tsa.shop.service.DefaultProductMapper;
import com.tsa.shop.service.DefaultProductService;
import com.tsa.shop.service.ProductDtoExtractor;
import com.tsa.shop.web.impl.*;
import com.tsa.shop.web.interfaces.*;
import org.eclipse.jetty.util.security.Credential;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AppContext {

    private final static ConcurrentHashMap<Class<?>, Object> BEANS = new ConcurrentHashMap<>();

    public static <T> T get(Class<T> beanClass) {
        if (BEANS.containsKey(beanClass)) {
            return beanClass.cast(BEANS.get(beanClass));
        }
        throw new RuntimeException("Bean of [%s] is absent".formatted(beanClass.getName()));
    }

    static {
        UriCache cache = new UriCache().setUp();
        BEANS.put(UriCache.class, cache);

        //        Parse Args
        EnvironmentVariablesContext context = new DefaultEnvironmentVariablesContext(Property.PORT, Property.FILE_PROPERTY);
        ArgsParser argsParser = new DefaultArgsParser(context);
        argsParser.parse();
        PropertyReader propertyReader = new DefaultPropertyReader(context);

        //        Apache Commons DBCP + DataSource.java
        DbConnector dbConnector = new BasicDataSourceAdapter(propertyReader);

        //        flyway
        FlywayBridge flywayBridge = new DefaultFlywayBridge(propertyReader);
        flywayBridge.migrate();

//        Utility Classes
        ServletRequestParser servletRequestParser = new DefaultServletRequestParser(cache);
        PageGenerator pageGenerator = new DefaultPageGenerator();
        ResponseWriter responseWriter = new DefaultResponseWriter();
        ContentFileProvider contentFileProvider = new DefaultContentFileProvider();
        Response response = new DefaultResponse();
        BEANS.put(ServletRequestParser.class, servletRequestParser);
        BEANS.put(PageGenerator.class, pageGenerator);
        BEANS.put(ResponseWriter.class, responseWriter);
        BEANS.put(ContentFileProvider.class, contentFileProvider);
        BEANS.put(Response.class, response);

//        NEW DAO Product
        ProductRowFetcher productRowFetcher = new DefaultProductRowFetcher();
        PSResolver psResolver = new DefaultPSResolver();
        ProductDao productDao = new JdbcProductDao(dbConnector, psResolver, productRowFetcher);
        DtoExtractor dtoExtractor = new ProductDtoExtractor();
        ProductMapper productMapper = new DefaultProductMapper();
        ProductService productService = new DefaultProductService(productDao, productMapper, dtoExtractor);
        BEANS.put(ProductService.class, productService);
//        Logging
        DomainLogger domainLogger = new DomainLoggerImpl();
        ExceptionInfoExtractor exceptionInfoExtractor = new ExceptionInfoExtractorImpl();
        LogMessageGenerator logMessageGenerator = new LogMessageGeneratorImpl(exceptionInfoExtractor);
        BEANS.put(DomainLogger.class, domainLogger);
        BEANS.put(LogMessageGenerator.class, logMessageGenerator);
//        Authentication
        LogInFactory logInFactory = new LogInFactoryImpl();
        UserRepository userRepository = new UserRepositoryImpl();
        initializeUsers(userRepository);
        TokenRepository tokenRepository = new DefaultTokenRepository();
        LogInFacade logInFacade = new LogInFacadeImpl(logInFactory, userRepository, tokenRepository);
        BEANS.put(LogInFacade.class, logInFacade);
        BEANS.put(TokenRepository.class, tokenRepository);
//        Cart
        CartRepository cartRepository = new CartRepository();
        CartService cartService = new CartService(cartRepository, tokenRepository, dtoExtractor);
        BEANS.put(CartRepository.class, cartRepository);
        BEANS.put(CartService.class, cartService);
//        Logout
        LogoutService logoutService = new DefaultLogoutService(tokenRepository);
        BEANS.put(LogoutService.class, logoutService);
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
