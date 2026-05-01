package is.pig.minecraft.api;

import java.util.UUID;

public record BlameData(UUID authorUuid, String authorName, String action, String worldId, BlockPos pos) {
}
