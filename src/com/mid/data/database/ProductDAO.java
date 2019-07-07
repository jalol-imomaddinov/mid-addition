package com.mid.data.database;

import com.mid.data.model.ProductModel;
import java.sql.SQLException;
import java.util.List;
import javafx.scene.image.Image;

/**
 * @author
 */
public interface ProductDAO {

    /**
     * **
     *
     * CATALOG DAO
     *
     * @param current asas
     * @param max
     * @param type
     * @throws SQLException
     */
    public void initProductReader(int current, int max, int type) throws SQLException;

    public ProductModel readProduct(ProductModel model) throws SQLException;

    public ProductModel getProductByName(String name) throws SQLException;

    public void saveProduct(ProductModel product) throws SQLException;

    public void updateProduct(ProductModel product) throws SQLException;

    public void deleteProduct(ProductModel product) throws SQLException;

    public int itemsCount(int type) throws SQLException;

    public Image loadProductImage(String catalog);
}
