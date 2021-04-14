/**
 * 
 */
package org.lsmr.selfcheckout.control;

import java.util.ArrayList;

import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.control.Checkout.PayingState;
import org.lsmr.selfcheckout.control.gui.StateHandler.StateUpdateListener;
import org.lsmr.selfcheckout.control.gui.statedata.BalanceStateData;
import org.lsmr.selfcheckout.control.gui.statedata.BooleanStateData;
import org.lsmr.selfcheckout.control.gui.statedata.BuyBagStateData;
import org.lsmr.selfcheckout.control.gui.statedata.InsertBarcodedProductData;
import org.lsmr.selfcheckout.control.gui.statedata.InsertPLUProductData;
import org.lsmr.selfcheckout.control.gui.statedata.KeypadStateData;
import org.lsmr.selfcheckout.control.gui.statedata.ListProductStateData;
import org.lsmr.selfcheckout.control.gui.statedata.LookupStateData;
import org.lsmr.selfcheckout.control.gui.statedata.MemberStateData;
import org.lsmr.selfcheckout.control.gui.statedata.ProductStateData;
import org.lsmr.selfcheckout.control.gui.statedata.PurchasingStateData;
import org.lsmr.selfcheckout.control.gui.statedata.AttendantLogInData;
import org.lsmr.selfcheckout.control.gui.statedata.AttendantStateData;
import org.lsmr.selfcheckout.control.gui.statedata.RequestPricePerBagData;
import org.lsmr.selfcheckout.control.gui.statedata.ScannedItemsRequestData;
import org.lsmr.selfcheckout.control.gui.statedata.StateData;
import org.lsmr.selfcheckout.devices.SimulationException;
import org.lsmr.selfcheckout.external.ProductDatabases;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import org.lsmr.selfcheckout.products.Product;

/**
 * @author charl
 * @date Apr. 12, 2021
 */
public class GUIupdateListener implements StateUpdateListener {
	private Checkout c;

	public GUIupdateListener(Checkout c) {
		this.c = c;
	}
	
	@Override
	public void onStateUpdate(StateData<?> data) {
		if (data instanceof KeypadStateData) { // keypad state
			int pluCode = (int) data.obtain();
			try {
				PriceLookupCode code = new PriceLookupCode(String.valueOf(pluCode));
				c.guiController
						.notifyDataUpdate(new ProductStateData(ProductDatabases.PLU_PRODUCT_DATABASE.get(code)));
			} catch (SimulationException e) {
				// no item in database - notify null
				c.guiController.notifyDataUpdate(null);
			}

		} else if (data instanceof ScannedItemsRequestData) { // buying state is requesting for data
			c.guiController.notifyDataUpdate(new ListProductStateData(c.getProductsAdded()));

		} else if (data instanceof BalanceStateData) { // buying state is requesting for total balance
			c.guiController.notifyDataUpdate(new BalanceStateData(c.getBalance().floatValue()));

		} else if (data instanceof MemberStateData) { // member card state
			String memberNum = (String) data.obtain();
			try {
				c.guiController.notifyDataUpdate(new BooleanStateData(c.enterMembershipCardInfo(memberNum)));
			} catch (CheckoutException e) {
				System.err.println(
						"Attempting to enter membership when already logged in. Check if the GUI is properly implemented.");
			}

		} else if (data instanceof LookupStateData) { // search has been made, so we return first search result
			ArrayList<Product> products = c.searchProductDatabase((String) data.obtain());
			if (products.isEmpty()) {
				c.guiController.notifyDataUpdate(null);
			} else {
				c.guiController.notifyDataUpdate(new ProductStateData(products.get(0)));
			}

		} else if (data instanceof InsertBarcodedProductData) { // inserts a barcoded product into the cart
			BarcodedProduct p = (BarcodedProduct) data.obtain();

			c.addBalanceUnit(p.getPrice());
			c.addExpectedWeightOnScale(ProductWeightDatabase.PRODUCT_WEIGHT_DATABASE.get(p.getBarcode()));
			c.addBarcodedProductToList(p, ProductWeightDatabase.PRODUCT_WEIGHT_DATABASE.get(p.getBarcode()));

		} else if (data instanceof InsertPLUProductData) { // inserts PLU into the cart
			try {
				c.enterPLUCode(((PLUCodedProduct) data.obtain()).getPLUCode());
			} catch (CheckoutException e) {
				System.err.println("Unknown PLU product");
			}

		} else if (data instanceof RequestPricePerBagData) { // requesting price of bags
			c.guiController.notifyDataUpdate(new RequestPricePerBagData(c.getPricePerPlasticBag().floatValue()));

		} else if (data instanceof BuyBagStateData) { // set # of bags to purchase
			int bags = (int) data.obtain();
			if (bags > 0) c.usePlasticBags((int) data.obtain());


		} else if (data instanceof AttendantStateData) {
			Pair<Integer, Integer> payload = ((AttendantStateData) data).obtain();

			if (payload.first == AttendantStateData.APPROVE_WEIGHT) {
				c.approveWeightDiscrepency();
				
			} else if (payload.first == AttendantStateData.REMOVE) {
				c.deleteProductAdded(c.getProductsAdded().get(payload.second));
				c.guiController.notifyDataUpdate(new ListProductStateData(c.getProductsAdded()));
			}
		} else if( data instanceof AttendantLogInData) {
			Pair<Integer, Integer> payload = ((AttendantLogInData) data).obtain();
			if(c.getAttendantSystem().login(payload.first, payload.second)) {
				((AttendantLogInData) data).success();
				c.guiController.notifyDataUpdate(data);
			}else {
				c.guiController.notifyDataUpdate(data);
			}

		} else if (data instanceof PurchasingStateData) {
			// obtain method of payment
			PayingState state = (PayingState) data.obtain();
			try {
				c.startPayment(state);
			} catch (CheckoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
