package com.example.viewmodelandnavigation

import kotlin.random.Random

class StaffRepository {

    fun generateStaff(count: Int): List<Staff> {
        val fullNames = listOf(
            "Sergey Usoltcev",
            "Darya Usoltceva",
            "Roman Samoilov",
            "Roman Strelnik",
            "Aleksandr Vasiljev",
            "Nikolaj Drudginin"
        )
        val positions = listOf(
            "Press-atashe",
            "Call-center manager",
            "Marketer",
            "Trainer"
        )
        val phoneNumbers = listOf(
            "8 (923) 342-21-21",
            "8 (913) 342-41-11",
            "8 (913) 342-41-11",
            "8 (960) 106-06-07",
            "8 (913) 223-06-07",
            "8 (966) 223-23-54"
        )
        val emails = listOf(
            "test@mail.ru",
            "mail@mail.ru",
            "text@gmail.com"
        )
        val photoLinks = listOf(
            "https://www.nsktv.ru/upload/resize_cache/iblock/6df/450_450_0/6df7766c68f1c30d7cd674d58da8ba1c.png",
            "https://sun9-2.userapi.com/impf/K4bTKaWxLPFkw8wHwp0F7ZtH0CEeXXt54AwSxA/Kh554yEJ3ic.jpg?size=1439x2160&quality=95&sign=ab4288b7fcae7bb499c7f47558cf14f8&type=album",
            "https://sun9-16.userapi.com/impg/c858528/v858528522/152613/1Nmu87A22PM.jpg?size=1440x2160&quality=96&sign=5e9467fc6ac620bf5b388925fa0ab9ea&type=album",
            "https://sun9-81.userapi.com/impf/c840527/v840527009/28db3/rCIb7tnnOcc.jpg?size=588x588&quality=96&sign=793ffcdee101edaa71b3b466f4caae60&type=album",
            "https://sun9-40.userapi.com/impf/c834104/v834104213/1ff71/PvIv_eAEKxk.jpg?size=1944x1944&quality=96&sign=489afce38968a41060288ea4926dc4a1&type=album",
            "https://sun9-29.userapi.com/impf/DWWiINEiQ7uOcevlbTfnjIha8MSKAxpmfelP1A/SPYTZbHTKd0.jpg?size=960x1200&quality=96&sign=5d81648bb18cd4b64cbe833f6f393e2b&type=album",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQHCZuslFbn42wwA9qw6ywBERhtpr_yOFy3Cw&usqp=CAU",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTvWNvxqwJyqQtJQLKuWSGDQaxDG0JQ1LHbV_ffZ1qdFc85UtnfLu1D2IbsKPdIqnUtyE8&usqp=CAU"
        )
        return (0 until count).map {
            val id = it.toLong()
            val fullName = fullNames.random()
            val position = positions.random()
            val phoneNumber = phoneNumbers.random()
            val photoLink = photoLinks.random()
            val isManagementTeam = Random.nextBoolean()
            if (isManagementTeam) {
                Staff.Manager(
                    id = id,
                    fullName = fullName,
                    position = position,
                    isManagementTeam = isManagementTeam,
                    phoneNumber = phoneNumber,
                    emailAddress = emails.random(),
                    photoLink = photoLink
                )
            } else {
                Staff.Employee(
                    id = id,
                    fullName = fullName,
                    position = position,
                    isManagementTeam = isManagementTeam,
                    phoneNumber = phoneNumber,
                    photoLink = photoLink
                )
            }
        }
    }

    fun createStaff(): Staff {
        return generateStaff(1).first().let {
            when (it) {
                is Staff.Manager -> it.copy(id = Random.nextLong(Long.MAX_VALUE))
                is Staff.Employee -> it.copy(id = Random.nextLong(Long.MAX_VALUE))
            }
        }
    }

    fun deleteStaff(staff: List<Staff>, position: Int): List<Staff> {
        return staff.filterIndexed { index, _ -> index != position }
    }

    fun getStaff(staff: List<Staff>, id: Long): Staff = staff.first {
        when (it) {
            is Staff.Employee -> it.id == id
            is Staff.Manager -> it.id == id
        }
    }
}