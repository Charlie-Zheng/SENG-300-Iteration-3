package org.lsmr.selfcheckout.control;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import org.lsmr.selfcheckout.products.BarcodedProduct;

public class ProductTableModel extends AbstractTableModel {
	
	private final ArrayList<ReceiptItem> productsAddedList = new ArrayList<ReceiptItem>();
	

	@Override
	public int getRowCount() {
		return productsAddedList.size();
	}

	@Override
	public int getColumnCount() {
		return 2; // name, cost
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		ReceiptItem item = productsAddedList.get(rowIndex);
		
		if (columnIndex == 0) { // return scanned item name
			// error prone here, since ReceiptItem has a Product type instead of BarcodedProduct. we assume the Checkout code
			// will stay the same (even though realistically it may not).
			return ((BarcodedProduct) item.product).getDescription();
		
		} else if (columnIndex == 1) { // return cost of scanned item
			return item.totalPrice;
			
		} else {
			throw new RuntimeException("Column index [" + columnIndex + "] not defined! Is the GUI set up properly?");
		}
		
	}
	
	@Override	
	public String getColumnName(int col) {
		switch(col) {
		case 0: return "Product";
		case 1: return "Cost";
		
		default:
			throw new RuntimeException("Unknown column: " + col);
		}
	}
	
	
	public float getColumnWidthWeight(int col) { // 0.25 -> quarter size, 0.5 -> half size
		switch(col) {
		case 0: return 0.75f;
		case 1: return 0.25f;
		
		default:
			throw new RuntimeException("Unknown column: " + col);
		}
	}
	
	/**
	 * Refresh the scanned products table panel
	 * 
	 * @param scannedProducts the new scanned products list
	 */
	public void setProductScannedList(ArrayList<ReceiptItem> scannedProducts) {
		productsAddedList.clear();
		productsAddedList.addAll(scannedProducts);
		fireTableDataChanged();
	}


}
