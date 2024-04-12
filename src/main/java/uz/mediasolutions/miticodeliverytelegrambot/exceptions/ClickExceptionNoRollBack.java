package uz.mediasolutions.miticodeliverytelegrambot.exceptions;

import lombok.Getter;

/**
 * For errors that may occur when working with the Click LLC API <br/>
 *
 * @author Muhammad Mo'minov
 * @see <a href="https://docs.click.uz/en/click-api-error/">Click-API – Errors</a>
 * @since 7/15/2022
 */
@Getter
public class ClickExceptionNoRollBack extends RuntimeException {
    /**
     * Payment ID in CLICK system
     */
    private final int errorCode;

    /**
     * Identification of the code to complete the payment.
     */
    private final String errorNote;

    private ClickExceptionNoRollBack(int errorCode, String errorNote) {
        super(errorNote);
        this.errorCode = errorCode;
        this.errorNote = errorNote;
    }

    /**
     * Signature verification error
     *
     * @param errorNote SIGN CHECK FAILED!
     * @return
     */
    public static ClickExceptionNoRollBack signCheckFailed(String errorNote) {
        return new ClickExceptionNoRollBack(-1, errorNote);
    }

    /**
     * Invalid payment amount
     *
     * @param errorNote Incorrect parameter amount
     * @return
     */
    public static ClickExceptionNoRollBack incorrectPaymentAmount(String errorNote) {
        return new ClickExceptionNoRollBack(-2, errorNote);
    }

    /**
     * The requested action is not found
     *
     * @param errorNote Action not found
     * @return
     */
    public static ClickExceptionNoRollBack actionNotFound(String errorNote) {
        return new ClickExceptionNoRollBack(-3, errorNote);
    }

    /**
     * The transaction was previously confirmed (when trying to confirm or cancel the previously confirmed transaction)
     *
     * @param errorNote Already paid
     * @return
     */
    public static ClickExceptionNoRollBack alreadyPaid(String errorNote) {
        return new ClickExceptionNoRollBack(-4, errorNote);
    }


    /**
     * Do not find a user / order (check parameter merchant_trans_id)
     *
     * @param errorNote User does not exist
     * @return
     */
    public static ClickExceptionNoRollBack userDoesNotExist(String errorNote) {
        return new ClickExceptionNoRollBack(-5, errorNote);
    }

    public static ClickExceptionNoRollBack invoiceDoesNotExist(String errorNote) {
        return new ClickExceptionNoRollBack(-250, errorNote);
    }

    /**
     * The transaction is not found (check parameter merchant_prepare_id)
     *
     * @param errorNote Transaction does not exist
     * @return
     */
    public static ClickExceptionNoRollBack transactionDoesNotExist(String errorNote) {
        return new ClickExceptionNoRollBack(-6, errorNote);
    }

    /**
     * An error occurred while changing user data (changing account balance, etc.)
     *
     * @param errorNote Failed to update user
     * @return
     */
    public static ClickExceptionNoRollBack failedToUpdateUser(String errorNote) {
        return new ClickExceptionNoRollBack(-7, errorNote);
    }


    /**
     * The error in the request from CLICK (not all transmitted parameters, etc.)
     *
     * @param errorNote Error in request from click
     * @return
     */
    public static ClickExceptionNoRollBack clickError(String errorNote) {
        return new ClickExceptionNoRollBack(-8, errorNote);
    }


    /**
     * The transaction was previously canceled (When you attempt to confirm or cancel the previously canceled transaction)
     *
     * @param errorNote Transaction cancelled
     * @return
     */
    public static ClickExceptionNoRollBack transactionCancelled(String errorNote) {
        return new ClickExceptionNoRollBack(-9, errorNote);
    }

}
