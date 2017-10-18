package org.t246osslab.easybuggy4kt.exceptions

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import javax.swing.undo.UndoManager

@Controller
class CannotRedoExceptionController {

    @RequestMapping(value = "/cre")
    fun process() {
        UndoManager().redo()
    }
}