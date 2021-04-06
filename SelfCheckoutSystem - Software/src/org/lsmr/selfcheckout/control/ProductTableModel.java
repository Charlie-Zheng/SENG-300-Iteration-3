package org.lsmr.selfcheckout.control;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import org.lsmr.selfcheckout.products.BarcodedProduct;

public class ProductTableModel extends AbstractTableModel {
	
	private ArrayList<BarcodedProduct> productsAddedList;
	
	// TODO: Remove
	final Object[][] fakeData = new Object[][] {
		{"Col 1asdfasdf", "Col 2"},
		{"Col 2-1", "Col 2-2"}
	};
	

	@Override
	public int getRowCount() {
		return 2;
		//return productsAddedList.size();
	}

	@Override
	public int getColumnCount() {
		return 2; // name, cost
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return fakeData[rowIndex][columnIndex];
		
		
//		if (columnIndex == 0) { // return scanned item name
//			productsAddedList.get(rowIndex).getDescription();
//		
//		} else if (columnIndex == 1) { // return cost of scanned item
//			return productsAddedList.get(rowIndex).getPrice().toString();
//		}
		
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
	public void setProductScannedList(ArrayList<BarcodedProduct> scannedProducts) {
		productsAddedList = scannedProducts;
		fireTableDataChanged();
	}


}
