package com.example.bestofbehance.binding.mapper

import com.example.bestofbehance.binding.PeopleBinding
import com.example.bestofbehance.binding.ProfileBinding
import com.example.bestofbehance.extension.CurrentDate

object MapperForPeopleBinding {
    fun from(form: ProfileBinding, username: String?) =
        PeopleBinding(
            form.id!!,
            username,
            form.avatar,
            form.name,
            form.post,
            form.views,
            form.appreciations,
            form.followers,
            form.following,
            CurrentDate.getCurrentDateTime().toString()
        )
}