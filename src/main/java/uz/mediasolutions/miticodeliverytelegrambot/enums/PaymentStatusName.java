package uz.mediasolutions.miticodeliverytelegrambot.enums;

public enum PaymentStatusName {

    // YANGI OCHILGAN INVOICE HALI TO'LANMAGAN
    PENDING,

    // BEKOR QILINGAN INVOICE (BIZ TOMONIMIZDAN BEKOR QILINGAN)
    REJECTED,

    // QISMAN TO'LAGAN VA HOZIRDA HAM TO'LAYABDI
    PAYING,

    // INVOICE MUDDATI O'TIB KETGAN
    // PULNI QISMAN TO'LAGAN BO'LSA HAM MUDDATI O'TIB KETISHI MUMKIN
    EXPIRED,

    // TO'LAGAN
    PAID,

    // PUL QAYTARILISHINI KUTILYABDI
    PENDING_REFUND,

    // PUL QAYTARIB BERILDI
    REFUND

}