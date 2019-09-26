package com.example.bestofbehance.binding.mapper

import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.binding.ProjectsBinding

object MapperForCardBinding{
    fun from(form: ProjectsBinding) =
        CardBinding(
            form.id,
            form.bigImage,
            form.thumbnail,
            form.avatar,
            form.artistName,
            form.artName,
            form.views,
            form.appreciations,
            form.comments,
            form.username,
            form.published,
            form.url
        )
}