package com.example.playlistmaker.domain.mapper

abstract class BaseMapper<From, To> {
    abstract fun map(from: From): To
}