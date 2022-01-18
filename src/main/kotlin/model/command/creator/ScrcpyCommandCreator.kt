package model.command.creator

import model.entity.Device
import java.io.File

class ScrcpyCommandCreator(val path: String? = null) {
    fun create(context: Device.Context): List<String> {
        return buildList {
            if (path != null) {
                if (path.endsWith(File.separator)) {
                    add("$path$COMMAND_NAME")
                } else {
                    add("$path${File.separator}$COMMAND_NAME")
                }
            } else {
                add(COMMAND_NAME)
            }

            add(DEVICE_OPTION_NAME)
            add(context.device.id)

            val maxSize = context.maxSize
            if (maxSize != null) {
                add(MAX_SIZE_OPTION_NAME)
                add(maxSize.toString())
            }
        }
    }

    fun createRecord(context: Device.Context, fileName: String): List<String> {
        return buildList {
            if (path != null) {
                if (path.endsWith(File.separator)) {
                    add("$path$COMMAND_NAME")
                } else {
                    add("$path${File.separator}$COMMAND_NAME")
                }
            } else {
                add(COMMAND_NAME)
            }

            add(DEVICE_OPTION_NAME)
            add(context.device.id)

            val maxSize = context.maxSize
            if (maxSize != null) {
                add(MAX_SIZE_OPTION_NAME)
                add(maxSize.toString())
            }

            add(RECORD_OPTION_NAME)
            add(fileName)
        }
    }

    fun createHelp(): List<String> {
        return buildList {
            if (path != null) {
                if (path.endsWith(File.separator)) {
                    add("$path$COMMAND_NAME")
                } else {
                    add("$path${File.separator}$COMMAND_NAME")
                }
            } else {
                add(COMMAND_NAME)
            }
            add(HELP_OPTION_NAME)
        }
    }

    companion object {
        private const val COMMAND_NAME = "scrcpy"
        private const val DEVICE_OPTION_NAME = "-s"
        private const val MAX_SIZE_OPTION_NAME = "-m"
        private const val HELP_OPTION_NAME = "-h"
        private const val RECORD_OPTION_NAME = "-r"
    }
}