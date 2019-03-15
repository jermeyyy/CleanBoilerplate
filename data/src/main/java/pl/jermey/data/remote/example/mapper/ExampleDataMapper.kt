package pl.jermey.data.remote.example.mapper

import pl.jermey.data.mapper.EntityMapper
import pl.jermey.data.remote.example.response.ExampleResponse
import pl.jermey.domain.model.example.Post

class ExampleDataMapper : EntityMapper<ExampleResponse, List<Post>> {
    override fun mapFromRemote(type: ExampleResponse): List<Post> {
        return type
    }
}