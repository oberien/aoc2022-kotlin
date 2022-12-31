package day18

import Day

class Day18 : Day {
    override fun part1(input: String): String {
        val cubes = parse(input)
        return cubes.cubes.asSequence()
            .map {
                adjacent(it).count { !cubes.cubes.contains(it) }
            }.sum().toString()
    }

    override fun part2(input: String): String {
        val cubes = parse(input)
        return cubes.cubes.asSequence()
            .flatMap {
                adjacent(it).filter { isFree(it, cubes)!! }
            }.count().toString()
    }
}

private fun parse(input: String): Cubes =
    Cubes(
        input.lineSequence()
            .map { it.split(",") }
            .map { (x, y, z) -> UnitCube(x.toInt(), y.toInt(), z.toInt()) }
            .toSet(),
        mutableSetOf(),
        mutableSetOf(),
    )

private data class Cubes(
    val cubes: Set<UnitCube>,
    val bubbleCubes: MutableSet<UnitCube>,
    val freeCubes: MutableSet<UnitCube>,
) {
    val maxX = cubes.maxOf { it.x }
    val minX = cubes.minOf { it.x }
    val maxY = cubes.maxOf { it.y }
    val minY = cubes.minOf { it.y }
    val maxZ = cubes.maxOf { it.z }
    val minZ = cubes.minOf { it.z }
}

private data class UnitCube(
    val x: Int,
    val y: Int,
    val z: Int,
)

private fun adjacent(cube: UnitCube): List<UnitCube> {
    val left = UnitCube(cube.x - 1, cube.y, cube.z)
    val right = UnitCube(cube.x + 1, cube.y, cube.z)
    val front = UnitCube(cube.x, cube.y - 1, cube.z)
    val back = UnitCube(cube.x, cube.y + 1, cube.z)
    val up = UnitCube(cube.x, cube.y, cube.z - 1)
    val down = UnitCube(cube.x, cube.y, cube.z + 1)
    return listOf(left, right, front, back, up, down)
}

private fun isFree(cube: UnitCube, cubes: Cubes): Boolean? {
    val worklist = ArrayDeque<UnitCube>()
    val checked = mutableSetOf<UnitCube>()
    checked.add(cube)
    worklist.addLast(cube)

    do {
        val current = worklist.removeFirst()
        if (cubes.cubes.contains(current)) {
            continue
        }
        if (cubes.freeCubes.contains(current)) {
            cubes.freeCubes.add(cube)
            return true
        }
        if (cubes.bubbleCubes.contains(current)) {
            cubes.bubbleCubes.add(cube)
            continue
        }
        if (
            current.x > cubes.maxX || current.x < cubes.minX
            || current.y > cubes.maxY || current.y < cubes.minY
            || current.z > cubes.maxZ || current.z < cubes.minZ
        ) {
            cubes.freeCubes.add(current)
            cubes.freeCubes.add(cube)
            return true
        }

        for (adjacent in adjacent(current)) {
            if (!checked.contains(adjacent)) {
                checked.add(adjacent)
                worklist.addLast(adjacent)
            }
        }
    } while (worklist.isNotEmpty())

    cubes.bubbleCubes.add(cube)
    return false
}
