package com.nitintraders.v_beltorder.hilt

import com.nitintraders.v_beltorder.datasource.BeltOrdersDataSource
import com.nitintraders.v_beltorder.datasource.BeltOrdersDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindBeltOrdersDataSource(
        beltOrdersDataSourceImpl: BeltOrdersDataSourceImpl
    ): BeltOrdersDataSource

}
