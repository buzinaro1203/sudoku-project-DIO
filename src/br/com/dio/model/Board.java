package br.com.dio.model;

import java.util.List;

public class Board {
  private final List<List<Space>> spaces;

  public Board(List<List<Space>> spaces) {
    this.spaces = spaces;
  }

  public List<List<Space>> getSpaces() {
    return spaces;
  }

  public GameStatusEnum getStatus(){
    return
  }
}
