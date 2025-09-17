object MyAssetQuery {
    const val GET_MY_ASSET_SEQ = """
        select nextval('seq_my_asset')
    """
    
    const val GET_MY_ASSET_SUM = """
        select	b.accum_dt
            ,	a.asset_id
            ,	a.asset_nm
            ,	coalesce(b.total_sum_price, 0) as sum_price
        from	asset	a
        left outer join
                (
                    select	ma.accum_dt
                        ,	ma.asset_id
                        ,	a.asset_nm
                        ,	sum(ma.price * ma.qty) as total_sum_price
                    from	asset	a
                    inner join	my_asset_accum	ma
                    on		a.asset_id = ma.asset_id
                    where	1=1
                    and		ma.accum_dt = :procDt
                    group by ma.accum_dt, ma.asset_id, a.asset_nm
                ) b
        on		a.asset_id = b.asset_id
        where	1=1
        order by a.asset_id
    """

    const val GET_MY_ASSET_ACCUM_LIST = """
        select	a.*
            ,	mg.my_asset_group_nm
        from	(
                    select	ma.asset_id
                        ,	ma.my_asset_id
                        ,	ma.my_asset_nm
                        ,	ma.ticker
                        ,	ma.price
                        ,	ma.qty
                        , 	a.asset_nm
                        ,	ma.ticker
                        ,	ma.price_div_cd
                        ,	ma.exchange_rate_yn
                        ,	cast(maa.price * maa.qty as numeric) as sum_price
                        ,	ma.my_asset_group_id
                        ,	ma.cashable_yn
                        ,   max(maa.mod_dts) over()				 as my_asset_accum_dts
                    from	my_asset		ma
                    inner join 	my_asset_accum 	maa
                    on		ma.my_asset_id 	= maa.my_asset_id
                    inner join asset			a
                    on		ma.asset_id 	= a.asset_id
                    where	1=1
                    and		maa.accum_dt 	= :procDt
                ) a
        left outer join my_asset_group mg
        on 	a.my_asset_group_id = mg.my_asset_group_id
    """

    const val GET_MY_ASSET_LIST = """
        select	a.*
            ,	mg.my_asset_group_nm
        from	(
                    select	ma.asset_id
                        ,	ma.my_asset_id
                        ,	ma.my_asset_nm
                        ,	ma.ticker
                        ,	ma.price_div_cd
                        ,	ma.price
                        ,	ma.qty
                        , 	a.asset_nm
                        , 	ma.exchange_rate_yn
                        ,	ma.my_asset_group_id
                        ,   ma.cashable_yn
                        ,	now()					as my_asset_accum_dts
                    from	my_asset	ma
                    inner join asset		a
                    on		ma.asset_id = a.asset_id
                    where	1=1
                    and     (:my_asset_id = '' or ma.my_asset_id = :my_asset_id)
                ) a
        left outer join coin c
        on		a.ticker = upper(c.symbol)
        and		a.asset_id = '3'
        left outer join my_asset_group mg
        on 		a.my_asset_group_id = mg.my_asset_group_id
        where	1=1
    """

    const val UPDATE_MY_ASSET_ACCUM = """
        update  my_asset_accum
        set     my_asset_nm = :#{#entity.myAssetNm}
            ,   asset_id = :#{#entity.assetId}
            ,   ticker = :#{#entity.ticker}
            ,   price = :#{#entity.price}
            ,   qty = :#{#entity.qty}
            ,   modpe_id = :#{#entity.modpeId}
            ,   mod_dts = :#{#entity.modDts}
        where   1=1
        and     accum_dt = :#{#entity.accumDt}
        and     my_asset_id = :#{#entity.myAssetId}
    """
}