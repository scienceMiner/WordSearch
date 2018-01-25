package com.ecollopy.ws.utils;

public class WordProps {

	public enum Alignment { HORIZONTAL, VERTICAL, DIAGONAL };
	public enum Order { BACKWARDS, FORWARDS };
	
	private Alignment _a;
	private Order _o;
	private int _boardSize;
	
	public WordProps(int size) {
		setAlignment(RandomHelper.setAlignment());
		setOrder(RandomHelper.setOrder());
		_boardSize = size;
	}

	public Alignment getAlignment() {
		return _a;
	}

	public void setAlignment(Alignment _a) {
		this._a = _a;
	}

	public Order getOrder() {
		return _o;
	}

	public void setOrder(Order _o) {
		this._o = _o;
	}
	
	public int getXLimit(int wordLength)
	{
		int xLimit = _boardSize;
		if (_a.equals(Alignment.HORIZONTAL) || _a.equals(Alignment.DIAGONAL))
		{
			xLimit = _boardSize - (wordLength - 1);
		}
		return xLimit;
	}
	
	public int getYLimit(int wordLength)
	{
		int yLimit = _boardSize;
		if (_a.equals(Alignment.VERTICAL) || _a.equals(Alignment.DIAGONAL))
		{
			yLimit = _boardSize - (wordLength - 1);
		}
		return yLimit;
	}
}
