package com.example.autos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AutosList {
    private List<Automobile> automobiles;
    public AutosList() {
        this.automobiles = new ArrayList<>();
    }

    public AutosList(List<Automobile> automobiles) {
        this.automobiles = automobiles;
    }

    public List<Automobile> getAutomobiles() {
        return automobiles;
    }

    public void setAutomobiles(List<Automobile> automobiles) {
        this.automobiles = automobiles;
    }

    public boolean isEmpty(){
        return this.automobiles.isEmpty();
    }
    @Override
    public String toString() {
        return "AutosList{" +
                "automobiles=" + automobiles +
                '}';
    }

    @Override
    public boolean equals (Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AutosList autosList = (AutosList) obj;
        return Objects.equals(automobiles, autosList.automobiles);
    }

    @Override
    public int hashCode(){
        return Objects.hash(automobiles);
    }
}
