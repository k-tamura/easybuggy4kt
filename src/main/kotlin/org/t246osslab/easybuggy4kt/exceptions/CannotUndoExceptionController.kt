package org.t246osslab.easybuggy4kt.exceptions

import javax.swing.undo.UndoManager

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class CannotUndoExceptionController {

    @RequestMapping(value = "/cue")
    fun process() {
        UndoManager().undo()
    }
}