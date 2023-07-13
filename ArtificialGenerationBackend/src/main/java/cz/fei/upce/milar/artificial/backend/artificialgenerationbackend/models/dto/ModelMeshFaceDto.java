package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.models.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.dto.DtoAncestor;

import java.util.ArrayList;
import java.util.List;

public class ModelMeshFaceDto extends DtoAncestor {
    private List<Integer> indices;

    public ModelMeshFaceDto() {
        indices = new ArrayList<>();
    }

    @JsonGetter("indices")
    public List<Integer> getIndices() {
        return indices;
    }

    @JsonSetter("indices")
    public void setIndices(List<Integer> indices) {
        this.indices = indices;
    }
}
