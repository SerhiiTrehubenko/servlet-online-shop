package com.tsa.shop;

import com.tsa.shop.database.interfaces.*;
import com.tsa.shop.database.repo.AbstractTsaRepository;
import com.tsa.shop.database.repo.ProductRepository;
import com.tsa.shop.database.util.*;
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
import com.tsa.shop.orm.impl.DefaultNameResolver;
import com.tsa.shop.orm.impl.DefaultEntityClassMeta;
import com.tsa.shop.orm.impl.DefaultSqlFactory;
import com.tsa.shop.orm.impl.DefaultSqlGenerator;
import com.tsa.shop.orm.interfaces.*;
import com.tsa.shop.orm.util.DefaultRequestDtoExtractor;
import com.tsa.shop.servlets.enums.UriPageConnector;
import com.tsa.shop.servlets.interfaces.*;
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

//        Servlets
        WebRequestHandler homeRequestHandler = new HomeWebRequestHandler(servletRequestParser, pageGenerator, responseWriter, response, contentFileProvider);
        WebRequestHandler productRequestHandler = new ProductWebRequestHandler<>(entityService, servletRequestParser, pageGenerator, response, responseWriter);

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
