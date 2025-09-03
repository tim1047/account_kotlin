object DivisionQuery {
    const val GET_DIVISIONS = """
        SELECT  *
        FROM    division
    """

    const val GET_DIVISIONS_SUM = """
        select	zz.*
        from	(
                    select 	z.division_id
                        ,	(
                                select	dd.division_nm
                                from	division	dd
                                where	1=1
                                and		dd.division_id = z.division_id
                            ) as division_nm
                        ,	sum(z.sum_price)  as sum_price
                    from	(
                                select	y.category_id
                                    ,	y.division_id
                                    ,	coalesce(sum(x.sum_price), 0)	as sum_price
                                from	(
                                            select	c.category_id
                                                , 	dcm.division_id
                                            from	category	c
                                            inner join	division_category_mpng	dcm
                                            on		c.category_id	= dcm.category_id
                                            where	1=1
                                        ) y
                                left outer join 
                                        (
                                            select	sum(case when a.point_price > 0 and a.division_id = '3' then a.price - a.point_price else a.price end)	   as sum_price
                                                ,	max(a.division_id) as division_id
                                                ,	a.category_id
                                            from	account			a
                                            where	1=1
                                            and		a.account_dt between :strtDt and :endDt
                                            group by a.category_id
                                                , a.category_seq
                                        ) x
                                on		y.category_id = x.category_id
                                and		y.division_id = x.division_id
                                where	1=1
                                group by y.category_id
                                    , 	 y.division_id
                            ) z
                    where	1=1		
                    group by z.division_id
                ) zz
        where	1=1
        order by zz.sum_price desc
    """
}