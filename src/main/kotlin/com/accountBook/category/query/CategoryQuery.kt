object CategoryQuery {
    const val GET_CATEGORYS = """
        SELECT  *
        FROM    category
    """

    const val GET_CATEGORY_SEQS_BY_CATEGORY_ID = """
        SELECT  category_id
            ,   category_seq
            ,   category_seq_nm
        FROM    category_dtl
        WHERE   1=1
        AND     category_id = :categoryId
    """
    
    const val GET_CATEGORYS_BY_DIVISION_ID = """
        SELECT  c.category_id
            ,   c.category_nm
            ,   d.division_id
            ,   d.division_nm
        FROM    division_category_mpng AS dcm
        INNER JOIN category AS c
        ON      dcm.category_id = c.category_id
        INNER JOIN division AS d
        ON      dcm.division_id = d.division_id
        WHERE   1=1
        AND     dcm.division_id = :divisionId
    """

    const val GET_CATEGORY_SUM = """
    select	zz.category_id
        ,	zz.category_nm
        ,	zz.division_id
        ,	zz.division_nm
        ,	zz.category_seq
        ,	zz.category_seq_nm
        ,	zz.sum_price
        ,	zz.total_sum_price
        ,	coalesce(cp.plan_price, 0)	as plan_price
    from	(
                select	z.category_id
                    ,	(
                            select	cc.category_nm
                            from	category	cc
                            where	1=1
                            and		cc.category_id = z.category_id
                        ) as category_nm
                    ,	z.division_id
                    ,	(
                            select	dd.division_nm
                            from	division	dd
                            where	1=1
                            and		dd.division_id = z.division_id
                        ) as division_nm
                    ,  	z.category_seq
                    , 	z.category_seq_nm
                    ,	max(z.account_yyyymm)	over()			as account_yyyymm
                    ,	z.sum_price 							as sum_price
                    ,	cast(sum(z.sum_price) over() as integer)				as total_sum_price
                from	(
                            select	y.category_id
                                ,	y.category_seq
                                , 	max(y.category_seq_nm)		as category_seq_nm
                                ,	y.division_id
                                ,	coalesce(sum(x.sum_price), 0) as sum_price
                                ,	to_char(now(), 'YYYYMM')	as account_yyyymm
                            from	(	
                                        select	cc.category_id
                                            ,	cc.division_id
                                            ,	coalesce(cd.category_seq, '0') as category_seq
                                            ,	cd.category_seq_nm
                                        from	(
                                                    select	c.category_id
                                                        , dcm.division_id
                                                    from	category	c
                                                    inner join division_category_mpng	dcm
                                                    on		c.category_id	= dcm.category_id 
                                                    where	1=1
                                                    and		dcm.division_id = :divisionId
                                                ) cc
                                        left outer join 
                                                category_dtl	cd
                                        on		cc.category_id = cd.category_id
                                    ) y
                            left outer join 
                                    (
                                        select	sum(a.price)	   		as sum_price
                                            ,	a.division_id 			as division_id
                                            ,	a.category_id
                                            ,   case when a.category_seq = '' then '0' else a.category_seq end as category_seq
                                        from	(
                                                    select	case when a.point_price > 0 and a.division_id = '3' then a.price - a.point_price else a.price end as price
                                                        ,	a.division_id
                                                        ,	a.category_id
                                                        ,	a.category_seq
                                                        ,	a.account_dt
                                                    from	account			a
                                                    where	1=1
                                                    and		a.account_dt between :strtDt and :endDt
                                                    and 	a.division_id = :divisionId
                                                ) a
                                        where	1=1
                                        group by a.category_id
                                                , a.category_seq
                                                , a.division_id
                                    ) x
                            on		y.category_id = x.category_id
                            and		y.division_id = x.division_id
                            and		y.category_seq = x.category_seq
                            where	1=1
                            group by y.category_id
                                ,	 y.category_seq
                                , 	 y.division_id
                        ) z
                where	1=1
            ) zz
    left outer join
            category_plan	cp
    on		zz.category_id 	= cp.category_id
    and		zz.account_yyyymm = cp.plan_dt
    where	1=1
    order by zz.sum_price desc, cast(zz.category_id as integer)
    """

    const val GET_CATEGORY_SEQ_SUM = """
        select	y.category_id
            ,	(
                    select	cc.category_nm
                    from	category	cc
                    where	1=1
                    and		cc.category_id = y.category_id
                ) as category_nm
            ,	y.division_id
            ,	(
                    select	dd.division_nm
                    from	division	dd
                    where	1=1
                    and		dd.division_id = y.division_id
                ) as division_nm
            ,	y.category_seq
            ,	(
                    select	cd.category_seq_nm
                    from	category_dtl	cd
                    where	1=1
                    and		cd.category_id = y.category_id
                    and		cd.category_seq = y.category_seq
                ) as category_seq_nm
            , 	y.fixed_price_yn
            ,	y.sum_price
            ,	cast(sum(y.sum_price) over() as integer)				as total_sum_price
        from	(
                    select	cd.category_id
                        ,	cd.category_seq
                        ,	cd.fixed_price_yn
                        ,	x.division_id
                        ,	coalesce(sum(x.sum_price), 0) as sum_price
                    from	category_dtl	cd
                    left outer join
                            (
                                select	a.category_id
                                    ,	a.category_seq
                                    ,	a.division_id
                                    ,	sum(a.price) as sum_price
                                from	(
                                            select	case when a.point_price > 0 and a.division_id = '3' then a.price - a.point_price else a.price end	as price
                                                ,	a.division_id 
                                                ,	a.category_id
                                                ,	a.account_dt
                                                ,	a.category_seq
                                            from	account			a
                                            where	1=1
                                            and		a.account_dt between :strtDt and :endDt
                                            and 	a.division_id = :divisionId
                                        ) a
                                where	1=1
                                group by a.category_id
                                    ,	a.category_seq
                                    ,	a.division_id
                            ) x
                    on		x.category_id = cd.category_id
                    and		x.category_seq = cd.category_seq
                    where	1=1
                    group by cd.category_id
                        , cd.category_seq
                        , x.division_id
                ) y
        left outer join 
                division_category_mpng	dcm
        on		y.division_id = dcm.division_id
        and		y.category_id = dcm.category_id 		
        where	1=1
        order by y.sum_price desc
            , cast(y.category_id as integer)
            , y.category_seq
    """
}