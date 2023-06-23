package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.dto;

import java.util.List;

public class ObjectFaceDto extends DtoAncestor {
    private List<Integer> indices;

    public List<Integer> getIndices() {
        return indices;
    }

    public void setIndices(List<Integer> indices) {
        this.indices = indices;
    }
}
