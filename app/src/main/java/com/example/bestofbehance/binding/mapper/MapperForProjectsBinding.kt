package com.example.bestofbehance.binding.mapper

import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.binding.ProjectsBinding
import com.example.bestofbehance.extension.CurrentDate

object MapperForProjectsBinding {
    fun from(form: CardBinding) =
        ProjectsBinding(
            form.id!!,
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
            form.url,
            CurrentDate.getCurrentDateTime().toString()
        )
}