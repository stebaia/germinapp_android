package com.google.maps.android.compose.di

import android.content.Context
import it.bsdsoftware.germinapp_new.data.api.NetworkModule
import it.bsdsoftware.germinapp_new.data.datasource.GerminaLocalDataSource
import it.bsdsoftware.germinapp_new.data.datasource.GerminaRemoteDataSourceImpl
import it.bsdsoftware.germinapp_new.data.datasource.GerminaRepositoryImpl
import it.bsdsoftware.germinapp_new.data.datasource.GerminiLocalDataSourceImpl
import it.bsdsoftware.germinapp_new.data.sharedpreferences.Preferences
import it.bsdsoftware.germinapp_new.data.db.GerminiDatabase

object ServiceLocator {
    private var database: GerminiDatabase? = null
    private const val url = "http://apitestgermina.bsdsoftware.it/api"

    @Volatile
    private var networkModule: NetworkModule? = null

    @Volatile
    var postsRepository: GerminaRepositoryImpl? = null

    fun provideGerminaRepository(context: Context): GerminaRepositoryImpl {
        // useful because this method can be accessed by multiple threads
        synchronized(this) {
            return postsRepository ?: createGerminaRepository(context)
        }
    }

    private fun createGerminaRepository(context: Context): GerminaRepositoryImpl {
        networkModule = NetworkModule(context)
        val newRepo =
            GerminaRepositoryImpl(
                createGerminiLocalDataSource(context),
                GerminaRemoteDataSourceImpl(
                    networkModule!!,
                    url
                )
            )
        postsRepository = newRepo
        return newRepo
    }

    private fun createGerminiLocalDataSource(context: Context): GerminaLocalDataSource {
        val database = database ?: createDataBase(context)
        return GerminiLocalDataSourceImpl(
            database,
            Preferences(context = context)
        )
    }

    private fun createDataBase(context: Context): GerminiDatabase {
        val result = GerminiDatabase.getDatabase(context)
        database = result
        return result
    }
}