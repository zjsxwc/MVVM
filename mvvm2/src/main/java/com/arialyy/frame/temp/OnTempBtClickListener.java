package com.arialyy.frame.temp;

public interface OnTempBtClickListener {
    /**
     * @param type {@link ITempView#ERROR}, {@link ITempView#DATA_NULL}, {@link ITempView#LOADING}
     */
    public void onBtTempClick(int type);
}