package com.mid.ui.contract.print;

import com.mid.data.common.ProductType;
import com.mid.data.database.DataAccessHelper;
import com.mid.data.database.DynamicDAO;
import com.mid.data.database.ProductDAO;
import com.mid.data.database.WorkerDAO;
import com.mid.data.model.FullContractModel;
import com.mid.data.model.WorkerModel;
import com.mid.util.DateUtil;
import com.mid.util.StringUtil;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class PreviewController implements Initializable {

    
    @FXML
    private HBox heightHbox;
    @FXML
    private StackPane printable;
    @FXML
    private AnchorPane firstPane;
//	private Label size;
    @FXML
    private Label ownerName;
    @FXML
    private Label ownerNumber;
    @FXML
    private Label endDate;
    @FXML
    private Label agreedAmound;
    @FXML
    private Label remaindAmound;
    @FXML
    private Label prepaidAmound;
    @FXML
    private Label startDate;
    @FXML
    private Label orderName;
    @FXML
    private Label orderNumber;
    @FXML
    private ImageView catalogImage;
    @FXML
    private Label catalogNumber;
    @FXML
    private Label welderPay;
    @FXML
    private Label painterPay;
//	private Label size1;
    @FXML
    private Label ownerName1;
    @FXML
    private Label ownerNumber1;
    @FXML
    private Label endDate1;
    @FXML
    private Label agreedAmound1;
    @FXML
    private Label remaindAmound1;
    @FXML
    private Label prepaidAmound1;
    @FXML
    private Label startDate1;
    @FXML
    private Label info;
    @FXML
    private Label orderName1;
    @FXML
    private Label orderNumber1;
    @FXML
    private ImageView catalogImage1;
    @FXML
    private Label catalogNumber1;
    @FXML
    private Label note;

    private ProductDAO catalogDAO;
    private WorkerDAO workerDAO;

    private WritableImage previewImage;
    @FXML
    private Label width;
    @FXML
    private Label height;
    @FXML
    private Label bindedLabel;
    @FXML
    private Label width1;
    @FXML
    private Label height1;

    @FXML
    private Label height_text;
    @FXML
    private Label height_text1;
    @FXML
    private Label width_text;
    @FXML
    private Label width_text1;
    
    @FXML
    private Label homeLabel;
    @FXML
    private Label mobileLabel;
    @FXML
    private Label bindedLabel1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
	catalogDAO = DataAccessHelper.getDataAccessHelper().getCatalogDAO();
        workerDAO = DataAccessHelper.getDataAccessHelper().getWorkerDAO();
	previewImage = new WritableImage(535, 370);
    }

    public void loadModel(FullContractModel model) throws SQLException {

        switch(model.getProductType()) {
            case RAILING: {
                width_text.setText("Погонный метр:");
                width_text1.setText("Погонный метр:");
                width.setText(StringUtil.DoubletoString(model.getWidth()));
                width1.setText(StringUtil.DoubletoString(model.getWidth()));
                hideHeight();
                break;
            }
            case BENCH: {
                width_text.setText("Эни:");
                width_text1.setText("Эни:");
                width.setText(StringUtil.DoubletoString(model.getWidth()));
                width1.setText(StringUtil.DoubletoString(model.getWidth()));
                hideHeight();
                break;
            }
            case SWING: {
                width_text.setText("Эни:");
                width_text1.setText("Эни:");
                width.setText(StringUtil.DoubletoString(model.getWidth()));
                width1.setText(StringUtil.DoubletoString(model.getWidth()));
                height_text.setText("Буйи:");
                height_text1.setText("Буйи:");
                height.setText(StringUtil.DoubletoString(model.getHeight()));
                height1.setText(StringUtil.DoubletoString(model.getHeight()));
                showHeight();
                break;
            }
            case CAPRICORN: {
                width_text.setText("Эни:");
                width_text1.setText("Эни:");
                width.setText(StringUtil.DoubletoString(model.getWidth()));
                width1.setText(StringUtil.DoubletoString(model.getWidth()));
                height_text.setText("Девордан чикиши:");
                height_text1.setText("Девордан чикиши:");
                height.setText(StringUtil.DoubletoString(model.getHeight()));
                height1.setText(StringUtil.DoubletoString(model.getHeight()));
                showHeight();
                break;
            }
        }
        
	setOrderNumber("#" + Integer.toString(model.getNumber()));
	setOrderName(makeTitle(model));
	setCatalogNumber(model.getCatalog());
	setOwnerName(model.getOwnerName());
	setOwnerNumber(model.getOwnerNumber());

	setStartDate(DateUtil.viewFormat(model.getLimitStart()));
	setEndDate(DateUtil.viewFormat(model.getLimitEnd()));

	setAgreedAmound(StringUtil.toAmound(model.getAgreedAmound()));
	setPrepaidAmound(StringUtil.toAmound(model.getPrepaidAmound()));
	setRemaindAmound(StringUtil.toAmound(model.getRemaindAmound()));

	setCatalogImage(catalogDAO.loadProductImage(model.getCatalog()));

	setWelderPay(StringUtil.toAmound(model.getWelderPay()));
	setPainterPay(StringUtil.toAmound(model.getPainterPay()));
	note.setText(model.getNote());

	DynamicDAO dynamicDAO = DataAccessHelper.getDataAccessHelper().getDynamicDAO();
	String mobile = dynamicDAO.getMobile();
	String home = dynamicDAO.getHome();
	
	homeLabel.setText("Офис: " + home);
        
        WorkerModel bindedWorker = workerDAO.getWorkerById(model.getBindedWoker());
        if (bindedWorker != null) {
            setBindedWorker(bindedWorker.getName() + " (" + bindedWorker.getPhone() + ")");
        }
        else {
            setBindedWorker("Белгиланмаган");
        }

	firstPane.snapshot(new SnapshotParameters(), previewImage);
    }

    private void showHeight() {
        height_text.setVisible(true);
        height_text1.setVisible(true);
        height1.setVisible(true);
        height.setVisible(true);
    }
    
    private void hideHeight() {
        height_text.setVisible(false);
        height_text1.setVisible(false);
        height1.setVisible(false);
        height.setVisible(false);
    }

    public Image getPreview() {
	return previewImage;
    }

    public Node getPrintable() {
	return printable;
    }

    private void setSize(String width, String height) {
	this.width.setText(width);
	this.width1.setText(width);

	this.height.setText(height);
	this.height1.setText(height);
    }
    private void setOwnerName(String _ownerName) {
	ownerName.setText(_ownerName);
	ownerName1.setText(_ownerName);
    }

    private void setOwnerNumber(String _ownerNumber) {
	ownerNumber.setText(_ownerNumber);
	ownerNumber1.setText(_ownerNumber);
    }

    private void setStartDate(String _startDate) {
	startDate.setText(_startDate);
	startDate1.setText(_startDate);
    }

    private void setEndDate(String _endDate) {
	endDate.setText(_endDate);
	endDate1.setText(_endDate);
    }

    private void setAgreedAmound(String _agreedAmound) {
	agreedAmound.setText(_agreedAmound);
	agreedAmound1.setText(_agreedAmound);
    }

    private void setRemaindAmound(String _remaindAmound) {
	remaindAmound.setText(_remaindAmound);
	remaindAmound1.setText(_remaindAmound);
    }

    private void setPrepaidAmound(String _prepaidAmound) {
	prepaidAmound.setText(_prepaidAmound);
	prepaidAmound1.setText(_prepaidAmound);
    }

    private void setCatalogImage(Image _catalogImage) {
	catalogImage.setImage(_catalogImage);
	catalogImage1.setImage(_catalogImage);
    }

    private void setCatalogNumber(String _catalogNumber) {
	catalogNumber.setText(_catalogNumber);
	catalogNumber1.setText(_catalogNumber);
    }

    private void setWelderPay(String _welderPay) {
	welderPay.setText(_welderPay);
    }

    private void setPainterPay(String _painterPay) {
	painterPay.setText(_painterPay);
    }
    private void setOrderName(String makeTitle) {
	orderName.setText(makeTitle);
	orderName1.setText(makeTitle);
    }

    private void setOrderNumber(String makeTitle) {
	orderNumber.setText(makeTitle);
	orderNumber1.setText(makeTitle);
    }

    private void setBindedWorker(String text) {
        bindedLabel.setText(text);
        bindedLabel1.setText(text);
    }
    
    private String makeTitle(FullContractModel model) {

	ProductType productType = model.getProductType();

	String builder = productType.getName();
        
	return builder;
    }
}
