package com.nitintraders.order.hilt

import com.nitintraders.order.datasource.BeltOrdersDataSource
import com.nitintraders.order.datasource.BeltOrdersDataSourceImpl
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
