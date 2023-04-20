package com.tsa.shop;

import com.tsa.shop.database.interfaces.DbConnector;
import com.tsa.shop.database.interfaces.ResultSetParser;
import com.tsa.shop.database.repo.AbstractTsaRepository;
import com.tsa.shop.database.repo.ProductRepository;
import com.tsa.shop.database.util.DefaultDbConnector;
import com.tsa.shop.database.util.DefaultResultSetParser;
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
import com.tsa.shop.domain.impl.ProductWebRequestHandler;
import com.tsa.shop.domain.interfaces.EntityService;
import com.tsa.shop.domain.interfaces.WebRequestHandler;
import com.tsa.shop.domain.mappers.DefaultProductMapper;
import com.tsa.shop.domain.mappers.interfaces.Mapper;
import com.tsa.shop.domain.services.DefaultProductService;
import com.tsa.shop.orm.impl.DefaultEntityClassMeta;
import com.tsa.shop.orm.impl.DefaultSqlFactory;
import com.tsa.shop.orm.impl.DefaultSqlGenerator;
import com.tsa.shop.orm.interfaces.AbstractSqlFactory;
import com.tsa.shop.orm.interfaces.AbstractSqlGenerator;
import com.tsa.shop.orm.interfaces.EntityClassMeta;
import com.tsa.shop.orm.interfaces.RequestDtoExtractor;
import com.tsa.shop.orm.util.DefaultRequestDtoExtractor;
import com.tsa.shop.servlets.enums.UriPageConnector;
import com.tsa.shop.servlets.interfaces.*;
import com.tsa.shop.servlets.util.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class ServletStarter {
    private static UriCache cache;
    private static EnvironmentVariablesContext context;
    private static ArgsParser argsParser;
    private static PropertyReader propertyReader;
    private static DbConnector dbConnector;
    private static EntityClassMeta classMeta;
    private static AbstractSqlFactory sqlFactory;
    private static AbstractSqlGenerator sqlGenerator;
    private static ResultSetParser<Product> productResultSetParser;
    private static AbstractTsaRepository<Product> productRepository;
    private static ServletRequestParser servletRequestParser;
    private static PageGenerator pageGenerator;
    private static ResponseWriter responseWriter;
    private static ContentFileProvider contentFileProvider;
    private static Mapper<Product, ProductDto> mapper;
    private static RequestDtoExtractor<ProductDto> entityExtractor;
    private static EntityService<Product, ProductDto> entityService;
    private static WebRequestHandler homeRequestHandler;
    private static WebRequestHandler productRequestHandler;

    public static void main(String[] args) throws Exception {
        cache = new UriCache().setUp();

//        Parse Args
        context = new DefaultEnvironmentVariablesContext(Property.PORT, Property.FILE_PROPERTY);
        argsParser = new DefaultArgsParser(context);
        argsParser.parse(args);
        propertyReader = new DefaultPropertyReader(context);

//        Db connection
        dbConnector = new DefaultDbConnector(propertyReader);

//        QueryGeneratorNew
        classMeta = new DefaultEntityClassMeta(Product.class);
        sqlFactory = new DefaultSqlFactory(classMeta);
        sqlGenerator = new DefaultSqlGenerator(sqlFactory);

//        TsaRepository
        productResultSetParser = new DefaultResultSetParser<>(classMeta);
        productRepository = new ProductRepository<>(sqlGenerator, productResultSetParser, dbConnector);

//        Utility Classes
        servletRequestParser = new DefaultServletRequestParser(cache);
        pageGenerator = new DefaultPageGenerator();
        responseWriter = new DefaultResponseWriter();
        contentFileProvider = new DefaultContentFileProvider();
        Response response = new DefaultResponse();

        mapper = new DefaultProductMapper();
        entityExtractor = new DefaultRequestDtoExtractor<>(ProductDto.class);

//        Service
        entityService = new DefaultProductService<>(productRepository,mapper, entityExtractor);

//        Servlets
        homeRequestHandler = new HomeWebRequestHandler(servletRequestParser, pageGenerator, responseWriter, response, contentFileProvider);
        productRequestHandler = new ProductWebRequestHandler<>(entityService, servletRequestParser, pageGenerator, response, responseWriter);

//        Set Servlets
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        String homeUri =  UriPageConnector.HOME.getUri();
        servletContextHandler.addServlet(new ServletHolder(homeRequestHandler), homeUri);
        String fileUri =  UriPageConnector.SLASH.getUri();
        servletContextHandler.addServlet(new ServletHolder(homeRequestHandler), fileUri);

        String productsUri =  UriPageConnector.PRODUCTS.getUri() + "/*";
        servletContextHandler.addServlet(new ServletHolder(productRequestHandler), productsUri);

//        Start Application
        Server server = new Server(propertyReader.getPort());
        server.setHandler(servletContextHandler);

        server.start();
    }
}
