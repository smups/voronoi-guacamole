package com.smups.exceptions;

import com.smups.Canvas;

public class CanvasSizeMisMatchException extends Exception{

    public CanvasSizeMisMatchException(Canvas expected, Canvas result) {
        super(String.format(
            "Canvas sizes did not match. Expected size ({},{}) but recieved ({},{})",
            expected.rows, expected.cols,
            result.rows, result.cols
            )
        );
    }

    public CanvasSizeMisMatchException(Canvas expected, int given_rows, int given_cols) {
        super(String.format(
            "Canvas sizes did not match. Expected size ({},{}) but recieved ({},{})",
            expected.rows, expected.cols,
            given_rows, given_cols
            )
        );
    }
    
}