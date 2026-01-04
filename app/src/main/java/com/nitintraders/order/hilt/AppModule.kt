package com.nitintraders.order.hilt

import android.content.Context
import com.nitintraders.order.database.BeltOrderDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideBeltOrderDatabase(
        @ApplicationContext applicationContext: Context
    ): BeltOrderDatabase = BeltOrderDatabase.getInstance(applicationContext)

}
