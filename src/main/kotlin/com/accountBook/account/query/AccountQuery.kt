object AccountQuery {
    const val GET_ACCOUNTS = """
        select	(ROW_NUMBER() OVER()) AS seq
            ,	zz.*
        from	(
                    select	z.account_id
                        ,	z.account_dt
                        ,	z.division_id
                        ,	z.division_nm
                        , 	z.member_id
                        ,	z.member_nm
                        ,	z.payment_id
                        ,	z.payment_nm
                        ,	z.payment_type
                        ,	z.category_id
                        ,	z.category_nm
                        ,	z.category_seq
                        ,	coalesce(cd.category_seq_nm, '') as category_seq_nm
                        ,	z.price
                        ,	z.remark
                        ,	z.impulse_yn
                        ,	z.point_price
                    from	(
                                select	a.account_id
                                    ,	a.account_dt
                                    ,	a.division_id
                                    ,	d.division_nm
                                    ,	a.member_id
                                    ,	am.member_nm
                                    ,	a.payment_id
                                    ,	p.payment_nm
                                    ,	p.payment_type
                                    ,	c.category_nm
                                    ,	a.price
                                    ,	a.remark
                                    ,	a.impulse_yn
                                    ,	a.category_id
                                    ,	a.category_seq
                                    ,	a.point_price
                                from	account			a
                                inner join division		d
                                on		a.division_id 	= d.division_id 
                                inner join account_member	am
                                on		a.member_id   	= am.member_id
                                inner join payment			p
                                on		a.payment_id  	= p.payment_id
                                inner join	category		c
                                on		a.category_id 	= c.category_id
                                where	1=1
                                and		a.account_dt between :strtDt and :endDt
                                and		(:divisionId = '' or a.division_id = :divisionId)
                                and		(:memberId = '' or a.member_id = :memberId)
                                and		(:categoryId = '' or a.category_id = :categoryId)
                                and		(:categorySeq = '' or a.category_seq = :categorySeq)
                            ) z
                            left outer join category_dtl	cd
                            on 		z.category_id 	= cd.category_id
                            and		z.category_seq 	= cd.category_seq
                    where	1=1
                    and		(:fixedPriceYn = '' or coalesce(cd.fixed_price_yn, 'N') = :fixedPriceYn)
                    order by z.account_dt desc
                        ,	 z.account_id desc
                ) zz
        where	1=1
    """

    const val GET_ACCOUNT_SEQ = """
        select nextval('seq_account')
    """
}