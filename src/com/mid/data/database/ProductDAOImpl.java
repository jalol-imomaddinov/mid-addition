package com.mid.data.database;

import com.mid.data.common.ProductType;
import com.mid.data.model.ProductModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.image.Image;

/**
 * @author
 */
public class ProductDAOImpl implements ProductDAO {

    private static final String GET_PRODUCT_WITHOUT_FILTER = "select * from Products where type = ? order by number DESC limit ?,?;";

    private static final String SET_PRODUCT = "insert into Products (number, type, price, painter, welder) values(?, ?, ?, ?, ?)";

    private static final String UPDATE_PRODUCT = "update Products set number = ?, type = ?, price = ?, painter = ?, welder = ? where id = ?";

    private static final String DELETE_PRODUCT = "delete from Products where id = ?";

    private ResultSet resultSet;
    private PreparedStatement statement;

    @Override
    public void initProductReader(int current, int max, int type) throws SQLException {

	String query = GET_PRODUCT_WITHOUT_FILTER;

	statement = DatabaseHandler.getCatalogConnection().prepareStatement(query);
	statement.setInt(1, type);
	statement.setInt(2, current * max);
	statement.setInt(3, max);

	resultSet = statement.executeQuery();
    }

    @Override
    public ProductModel readProduct(ProductModel product) throws SQLException {
	if (!resultSet.next()) {
	    resultSet.close();
	    statement.close();
	    return null;
	}

	product.setId(resultSet.getInt(1));
	product.setCatalog(resultSet.getString(2));
	product.setProductType(ProductType.productById(resultSet.getInt(3)));
	product.setPrice(resultSet.getDouble(4));
	product.setWelder(resultSet.getDouble(5));
	product.setPainter(resultSet.getDouble(6));

	product.init();

	return product;
    }

    @Override
    public ProductModel getProductByName(String name) throws SQLException {
	PreparedStatement stmt = DatabaseHandler.getCatalogConnection().prepareStatement("select * from Products where number = ?;");
	stmt.setString(1, name);

	ResultSet result = stmt.executeQuery();

	if (!result.next()) {
	    return null;
	}
	return getProduct(result);
    }

    @Override
    public void saveProduct(ProductModel product) throws SQLException {
	PreparedStatement stmt = DatabaseHandler.getCatalogConnection().prepareStatement(SET_PRODUCT);
	stmt.setString(1, product.getCatalog());
	stmt.setInt(2, product.getProductType().getId());
        stmt.setDouble(3, product.getPrice());
        stmt.setDouble(4, product.getWelder());
        stmt.setDouble(5, product.getPainter());

	boolean execute = stmt.execute();
    }

    @Override
    public void updateProduct(ProductModel product) throws SQLException {
	PreparedStatement stmt = DatabaseHandler.getCatalogConnection().prepareStatement(UPDATE_PRODUCT);
	stmt.setString(1, product.getCatalog());
	stmt.setInt(2, product.getProductType().getId());
        stmt.setDouble(3, product.getPrice());
        stmt.setDouble(4, product.getPainter());
        stmt.setDouble(5, product.getWelder());
	stmt.setDouble(6, product.getId());

	boolean execute = stmt.execute();
    }

    @Override
    public void deleteProduct(ProductModel product) throws SQLException {
	PreparedStatement stmt = DatabaseHandler.getCatalogConnection().prepareStatement(DELETE_PRODUCT);
	stmt.setInt(1, product.getId());
	boolean result = stmt.execute();
    }

    @Override
    public Image loadProductImage(String catalog) {
	Image image = new Image("file:" + Paths.IMAGE_PATH + catalog + ".jpg");
	return image;
    }

    @Override
    public int itemsCount(int type) throws SQLException {
	PreparedStatement stmt = DatabaseHandler.getCatalogConnection().prepareStatement("select count(*) as count from Products where type = ?;");
	stmt.setInt(1, type);
	ResultSet result = stmt.executeQuery();
	int count = result.getInt(1);
	result.close();

	return count;
    }

    private ProductModel getProduct(ResultSet result) throws SQLException {

	ProductModel product = new ProductModel();

	product.setId(result.getInt(1));
	product.setCatalog(result.getString(2));
	product.setProductType(ProductType.productById(result.getInt(3)));
	product.setPrice(result.getDouble(4));
	product.setWelder(result.getDouble(5));
	product.setPainter(result.getDouble(6));

	return product;
    }
}
