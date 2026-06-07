package ConzyNestapp.com.CozyNest.Controller;

import ConzyNestapp.com.CozyNest.Dto.Request.UpdateInventoryDto;
import ConzyNestapp.com.CozyNest.Dto.Response.InventoryDto;
import ConzyNestapp.com.CozyNest.Service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Component
@RequestMapping("/admin/inventory")
@Slf4j
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping("/{roomId}/allInventory")
    public ResponseEntity<List<InventoryDto>> getAllInventoryForRoom(@PathVariable("roomId") Long roomId){
          List<InventoryDto> inventories=inventoryService.getAllInventoryForRoom(roomId);
       return  ResponseEntity.ok(inventories);
    }

    @PutMapping("/{roomId}/allInventory")
    public ResponseEntity<Void> UpdateInventory(@PathVariable("roomId") Long roomId, @RequestBody UpdateInventoryDto updateInventoryDto){
        inventoryService.UpdateInventory(roomId,updateInventoryDto);
        return  ResponseEntity.noContent().build();
    }

}
