object MemberQuery {
    const val GET_MEMBERS = """
        SELECT  *
        FROM    account_member
    """

    const val GET_MEMBERS_SUM = """
        select	b.*
        from	(
                    select	sum(case when a.point_price > 0 and a.division_id = '3' then a.price - a.point_price else a.price end)	   as sum_price
                        ,	a.member_id
                        ,	(
                                select	member_nm
                                from	account_member	am
                                where	1=1
                                and		am.member_id = a.member_id
                            )	as member_nm
                    from	account			a
                    where	1=1
                    and		a.account_dt between :strtDt and :endDt
                    and		a.division_id = :divisionId
                    group by a.member_id
                ) b
        where	1=1
        order by b.sum_price desc
    """
}