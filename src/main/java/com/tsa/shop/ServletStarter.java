package com.tsa.shop;

import com.tsa.shop.database.interfaces.*;
import com.tsa.shop.database.repo.AbstractTsaRepository;
import com.tsa.shop.database.repo.ProductRepository;
import com.tsa.shop.database.util.*;
import com.tsa.shop.database.versioncontrol.DefaultFlywayBridge;
import com.tsa.shop.database.versioncontrol.FlywayBridge;
import com.tsa.shop.domain.argsparser.enums.Property;
import com.tsa.shop.domain.argsparser.impl.DefaultArgsParser;
import com.tsa.shop.domain.argsparser.impl.DefaultEnvironmentVariablesContext;
import com.tsa.shop.domain.argsparser.impl.DefaultPropertyReader;
import com.tsa.shop.domain.argsparser.interfaces.ArgsParser;
import com.tsa.shop.domain.argsparser.interfaces.EnvironmentVariablesContext;
import com.tsa.shop.domain.argsparser.interfaces.PropertyReader;
import com.tsa.shop.domain.dto.ProductDto;
import com.tsa.shop.domain.entity.Product;
import com.tsa.shop.domain.impl.HomeWebRequestHandler;
import com.tsa.shop.domain.interfaces.EntityService;
import com.tsa.shop.domain.interfaces.WebRequestHandler;
import com.tsa.shop.domain.logging.DomainLogger;
import com.tsa.shop.domain.logging.DomainLoggerImpl;
import com.tsa.shop.domain.logmessagegenerator.ExceptionInfoExtractor;
import com.tsa.shop.domain.logmessagegenerator.ExceptionInfoExtractorImpl;
import com.tsa.shop.domain.logmessagegenerator.LogMessageGenerator;
import com.tsa.shop.domain.logmessagegenerator.LogMessageGeneratorImpl;
import com.tsa.shop.domain.mappers.DefaultProductMapper;
import com.tsa.shop.domain.mappers.interfaces.Mapper;
import com.tsa.shop.domain.services.DefaultProductService;
import com.tsa.shop.orm.impl.DefaultNameResolver;
import com.tsa.shop.orm.impl.DefaultEntityClassMeta;
import com.tsa.shop.orm.impl.DefaultSqlFactory;
import com.tsa.shop.orm.impl.DefaultSqlGenerator;
import com.tsa.shop.orm.interfaces.*;
import com.tsa.shop.orm.util.DefaultRequestDtoExtractor;
import com.tsa.shop.servlets.enums.UriPageConnector;
import com.tsa.shop.servlets.interfaces.*;
import com.tsa.shop.servlets.servlet.*;
import com.tsa.shop.servlets.util.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

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

//        QueryGeneratorNew
        EntityClassMeta classMeta = new DefaultEntityClassMeta(Product.class);
        PreparedStatementDataInjector<Product> preparedStatementDataInjector = new DefaultPreparedStatementDataInjector<>(classMeta);
        IdResolver idResolver = new IdResolverImpl();
        NameResolver resolver = new DefaultNameResolver(classMeta);
        AbstractSqlFactory sqlFactory = new DefaultSqlFactory(resolver);
        AbstractSqlGenerator sqlGenerator = new DefaultSqlGenerator(sqlFactory);

//        TsaRepository

        ResultSetMethodProvider methodProvider = new ResultSetMethodProviderImpl();
        RowDataExtractor dataExtractor = new RowDataExtractorImpl(classMeta, methodProvider);
        EntityRowFetcher<Product> productEntityRowFetcher = new DefaultEntityRowFetcher<>(classMeta, dataExtractor);
        AbstractTsaRepository<Product> productRepository = new ProductRepository<>(sqlGenerator,
                productEntityRowFetcher, dbConnector, preparedStatementDataInjector, idResolver);

//        Utility Classes
        ServletRequestParser servletRequestParser = new DefaultServletRequestParser(cache);
        PageGenerator pageGenerator = new DefaultPageGenerator();
        ResponseWriter responseWriter = new DefaultResponseWriter();
        ContentFileProvider contentFileProvider = new DefaultContentFileProvider();
        Response response = new DefaultResponse();

        Mapper<Product, ProductDto> mapper = new DefaultProductMapper();
        RequestDtoExtractor<ProductDto> entityExtractor = new DefaultRequestDtoExtractor<>(ProductDto.class);

//        Service
        EntityService<Product, ProductDto> entityService = new DefaultProductService<>(productRepository, mapper, entityExtractor);

//        Logging
        DomainLogger domainLogger = new DomainLoggerImpl();
        ExceptionInfoExtractor exceptionInfoExtractor = new ExceptionInfoExtractorImpl();
        LogMessageGenerator logMessageGenerator = new LogMessageGeneratorImpl(exceptionInfoExtractor);

//        Servlets
        WebRequestHandler homeRequestHandler = new HomeWebRequestHandler(servletRequestParser, pageGenerator, responseWriter, response, domainLogger, logMessageGenerator, contentFileProvider);

        WebRequestHandler productFindAllRequestHandler = new ProductFindAllWebRequestHandler<>(servletRequestParser, pageGenerator, responseWriter, response, domainLogger, logMessageGenerator, entityService);
        WebRequestHandler productDeleteRequestHandler = new ProductDeleteWebRequestHandler<>(servletRequestParser, pageGenerator, responseWriter, response, domainLogger, logMessageGenerator, entityService);
        WebRequestHandler productUpdateRequestHandler = new ProductUpdateWebRequestHandler<>(servletRequestParser, pageGenerator, responseWriter, response, domainLogger, logMessageGenerator, entityService);
        WebRequestHandler productAddRequestHandler = new ProductAddWebRequestHandler<>(servletRequestParser, pageGenerator, responseWriter, response, domainLogger, logMessageGenerator, entityService);
        WebRequestHandler pageNotFoundRequestHandler = new PageNotFoundHandler(servletRequestParser, pageGenerator, responseWriter, response, domainLogger, logMessageGenerator);

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
        servletContextHandler.addServlet(new ServletHolder(productAddRequestHandler), addProductGet);

        String notFoundUri = UriPageConnector.PRODUCTS.getUri() + "/*";
        servletContextHandler.addServlet(new ServletHolder(pageNotFoundRequestHandler), notFoundUri);

//        Start Application
        Server server = new Server(propertyReader.getPort());
        server.setHandler(servletContextHandler);

        server.start();
    }
}
