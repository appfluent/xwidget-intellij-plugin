package us.appfluent.xwidget.utils

data class Version(
    val major: Int,
    val minor: Int,
    val patch: Int,
    val preRelease: List<String> = emptyList(),
    val build: List<String> = emptyList()
) : Comparable<Version> {

    companion object {
        fun parse(raw: String): Version {
            val mainAndBuild = raw.split("+", limit = 2)
            val mainAndPre = mainAndBuild[0].split("-", limit = 2)

            val coreParts = mainAndPre[0].split(".")
            val major = coreParts.getOrNull(0)?.toIntOrNull() ?: 0
            val minor = coreParts.getOrNull(1)?.toIntOrNull() ?: 0
            val patch = coreParts.getOrNull(2)?.toIntOrNull() ?: 0

            val preRelease = if (mainAndPre.size > 1) mainAndPre[1].split(".") else emptyList()
            val build = if (mainAndBuild.size > 1) mainAndBuild[1].split(".") else emptyList()

            return Version(major, minor, patch, preRelease, build)
        }
    }

    override operator fun compareTo(other: Version): Int {
        // Compare major/minor/patch first
        val cmp = compareValuesBy(this, other, Version::major, Version::minor, Version::patch)
        if (cmp != 0) return cmp

        // If equal, compare pre-release
        if (preRelease.isEmpty() && other.preRelease.isNotEmpty()) return 1 // release > prerelease
        if (preRelease.isNotEmpty() && other.preRelease.isEmpty()) return -1 // prerelease < release

        // Both have pre-release: compare identifiers
        for (i in 0 until maxOf(preRelease.size, other.preRelease.size)) {
            val a = preRelease.getOrNull(i)
            val b = other.preRelease.getOrNull(i)
            if (a == null) return -1
            if (b == null) return 1
            val numA = a.toIntOrNull()
            val numB = b.toIntOrNull()
            val idCmp = when {
                numA != null && numB != null -> numA.compareTo(numB)
                numA != null -> -1 // numbers < strings
                numB != null -> 1
                else -> a.compareTo(b)
            }
            if (idCmp != 0) return idCmp
        }

        // Build metadata is ignored for ordering
        return 0
    }
}

