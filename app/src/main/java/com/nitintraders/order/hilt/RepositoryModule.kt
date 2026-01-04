package com.nitintraders.order.hilt

import com.nitintraders.order.repository.BeltOrdersRepository
import com.nitintraders.order.repository.BeltOrdersRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule() {

    @Binds
    abstract fun bindBeltOrdersRepository(
        beltOrdersRepositoryImpl: BeltOrdersRepositoryImpl
    ): BeltOrdersRepository

}
