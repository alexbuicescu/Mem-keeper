package example.com.memkeeper.Utils;

import java.util.ArrayList;
import java.util.List;

import example.com.memkeeper.POJO.Memory;

/**
 * Created by Alexandru on 03-Apr-15.
 */
public class MemoriesUtils {
    private static List<Memory> memoryList;
    private static Memory currentMemory;

    public static List<Memory> getMemoryList() {
        if(memoryList == null)
        {
            memoryList = new ArrayList<>();
        }
        return memoryList;
    }

    public static void setMemoryList(List<Memory> memoryList) {
        MemoriesUtils.memoryList = memoryList;
    }

    public static Memory getCurrentMemory() {
        return currentMemory;
    }

    public static void setCurrentMemory(Memory currentMemory) {
        MemoriesUtils.currentMemory = currentMemory;
    }
}
