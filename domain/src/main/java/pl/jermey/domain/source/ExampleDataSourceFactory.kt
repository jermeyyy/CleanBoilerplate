package org.buffer.android.boilerplate.data.source

/**
 * Create an instance of a ExampleDataSource
 */
open class ExampleDataSourceFactory(
    private val exampleCacheDataSource: ExampleDataSource,
    private val exampleRemoteDataSource: ExampleDataSource) {

    /**
     * Returns a DataStore based on whether or not there is content in the cache and the cache
     * has not expired
     */
    open fun retrieveDataStore(isCached: Boolean): ExampleDataSource {
        if (isCached && !exampleCacheDataSource.isExpired()) {
            return retrieveCacheDataStore()
        }
        return retrieveRemoteDataStore()
    }

    /**
     * Return an instance of the Cache Data Store
     */
    open fun retrieveCacheDataStore(): ExampleDataSource {
        return exampleCacheDataSource
    }

    /**
     * Return an instance of the Remote Data Store
     */
    open fun retrieveRemoteDataStore(): ExampleDataSource {
        return exampleRemoteDataSource
    }

}