package cn.msa.msa_museum_server.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class BetaController {
  @GetMapping("/hello")
  public ResponseEntity<String> sayHello() {
    return ResponseEntity.ok("Hello, World!");
  }

  @PostMapping("/echo")
  public ResponseEntity<Map<String, String>> echoMessage(@RequestBody Map<String, String> payload) {
    Map<String, String> response = new HashMap<>();
    response.put("message", payload.getOrDefault("message", "No message received"));
    return ResponseEntity.ok(response);
  }

  @GetMapping("/greet/{name}")
  public ResponseEntity<String> greetUser(@PathVariable String name) {
    return ResponseEntity.ok("Hello, " + name + "!");
  }

  @PutMapping("/update")
  public ResponseEntity<String> updateData(@RequestParam String data) {
    return ResponseEntity.ok("Data updated to: " + data);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteItem(@PathVariable String id) {
    return ResponseEntity.ok("Item with ID " + id + " has been deleted.");
  }
}
