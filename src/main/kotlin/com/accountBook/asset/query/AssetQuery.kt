object AssetQuery {
    const val GET_ASSETS = """
        SELECT  *
        FROM    ASSET
        ORDER BY asset_id
    """
}