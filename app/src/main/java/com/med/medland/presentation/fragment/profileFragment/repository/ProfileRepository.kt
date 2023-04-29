package com.med.medland.presentation.fragment.profileFragment.repository

import com.med.medland.data.room.dao.MyInfoDao
import com.med.medland.data.room.table.MyInfoTable

class ProfileRepository(private val myInfoDao: MyInfoDao) {


    val readMyInfo : MyInfoTable = myInfoDao.getMyInfo()

}