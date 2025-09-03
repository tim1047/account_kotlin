object PaymentQuery {
    const val GET_PAYMENTS = """
        SELECT  *
        FROM    payment
    """

    const val GET_PAYMENTS_BY_MEMBER_ID = """
        SELECT  *
        FROM    payment
        WHERE   1=1
        AND     member_id = :memberId
    """
}