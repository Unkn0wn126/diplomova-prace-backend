from dataclasses import dataclass


@dataclass
class Transform2:
    x: float
    y: float


@dataclass
class Transform3:
    x: float
    y: float
    z: float


@dataclass
class Transform4:
    x: float
    y: float
    z: float
    w: float


@dataclass
class ModelMeshVertex:
    position: Transform3
    normal: Transform3
    texcoords: Transform2
    tangent: Transform3


@dataclass
class ModelMeshFace:
    indices: list[int]


@dataclass
class ModelMesh:
    vertices: list[ModelMeshVertex]
    faces: list[ModelMeshFace]


@dataclass
class Model:
    mesh: ModelMesh
    position: Transform3
    rotation: Transform4
    scale: Transform3


@dataclass
class GrabPoint:
    localPosition: Transform3
    localRotation: Transform4
    localScale: Transform3
