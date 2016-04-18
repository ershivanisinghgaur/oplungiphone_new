package com.firerox.oplungiphone.item;

public class ItemProductCategory {
	
	private int categoryId;
	private String categoryName;
	private String categoryImageUrl;
	
	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	
	
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public String getCategoryImageurl()
	{
		return categoryImageUrl;
		
	}
	
	public void setCategoryImageurl(String categoryImageUrl)
	{
		this.categoryImageUrl = categoryImageUrl;
	}

}
