package day07

import Day
import kotlin.math.abs

class Day07: Day {
    override fun part1(input: String): String {
        val root = Node("/", 0, NodeType.FOLDER)
        val commands = parseCommands(input.split("\n"))
        handleCommand(root, commands.iterator())
        val folders = folderSizes(root)
        return folders.asSequence()
            .filter { (_, size) -> size <= 100000 }
            .sumOf { (_, size) -> size }
            .toString()
    }

    override fun part2(input: String): String {
        val root = Node("/", 0, NodeType.FOLDER)
        val commands = parseCommands(input.split("\n"))
        handleCommand(root, commands.iterator())
        val folders = folderSizes(root)
        val free = 70000000 - folders.last().second
        val required = 30000000 - free
        return folders.asSequence()
            .filter { (_, size) -> size - required > 0 }
            .minOf { (_, size) -> size }
            .toString()
    }
}

private fun handleCommand(node: Node, commands: Iterator<Command>) {
    while (commands.hasNext()) {
        val command = commands.next()
        when {
            command.command == "cd" && command.args[0] == "/" -> {}
            command.command == "cd" && command.args[0] == ".." -> { return }
            command.command == "cd" -> {
                val folder = node.children.find { it.name == command.args[0] && it.type == NodeType.FOLDER }!!
                handleCommand(folder, commands)
            }
            command.command == "ls" -> {
                for (line in command.output) {
                    val split = line.split(" ")
                    when (split[0]) {
                        "dir" -> node.children.add(Node(line.substring(4), 0, NodeType.FOLDER))
                        else -> node.children.add(Node(split[1], split[0].toInt(), NodeType.FILE))
                    }
                }
            }
        }
    }
}


private fun parseCommands(input: List<String>): List<Command> {
    val commands = ArrayList<Command>()
    var index = 0;
    while (index < input.size) {
        val (idx, command) = Command.parse(input, index)
        index = idx
        commands.add(command)
    }
    return commands
}

private fun folderSizes(node: Node): List<Pair<Node, Int>> {
    val folders = ArrayList<Pair<Node, Int>>()
    folderSizesInternal(node, folders)
    return folders
}
private fun folderSizesInternal(node: Node, folders: MutableList<Pair<Node, Int>>): Int {
    var sum = 0
    for (child in node.children) {
        sum += when (child.type) {
            NodeType.FILE -> child.size
            NodeType.FOLDER -> folderSizesInternal(child, folders)
        }
    }
    folders.add(Pair(node, sum))
    return sum
}

private data class Node(
    val name: String,
    val size: Int,
    val type: NodeType,
    val children: MutableList<Node> = ArrayList(),
) {
    fun addChild(child: Node) {
        this.children.add(child)
    }
}

private enum class NodeType {
    FILE, FOLDER
}

private data class Command(
    val command: String,
    val args: List<String>,
    val output: List<String>,
) {
    companion object {
        fun parse(input: List<String>, index: Int): Pair<Int, Command> {
            val parts = input[index].splitToSequence(" ").iterator()
            val dollar = parts.next()
            val command = parts.next()
            val args = parts.asSequence().toList()
            if (dollar != "$") {
                throw IllegalArgumentException("command doesn't start with $: " + input[index])
            }

            val output = input.asSequence().drop(index + 1)
                .takeWhile { !it.startsWith("$") }
                .toList()

            return Pair(index + 1 + output.size, Command(command, args, output))
        }
    }
}

