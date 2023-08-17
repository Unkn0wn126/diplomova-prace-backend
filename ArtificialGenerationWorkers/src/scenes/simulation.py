import pybullet as p
import pybullet_data
from .domain import SceneModelsCoords, Transform3, Transform4


def simulate_physics(verts: list, model_coords: list[SceneModelsCoords], steps: int, time_step: float):
    p.connect(p.DIRECT)
    p.setAdditionalSearchPath(pybullet_data.getDataPath())
    p.setGravity(0, 0, -10)
    p.setTimeStep(time_step)
    p.setRealTimeSimulation(0)
    p.loadURDF("plane.urdf")
    box_ids = []

    for model_coord in model_coords:
        startPos = [model_coord.position.x, model_coord.position.z, model_coord.position.y]
        startOrientation = [model_coord.rotation.x, model_coord.rotation.z, model_coord.rotation.y,
                            model_coord.rotation.w]

        col_id = p.createCollisionShape(p.GEOM_MESH, vertices=verts, meshScale=[model_coord.scale.x,
                                                                                model_coord.scale.z,
                                                                                model_coord.scale.y])

        box_ids.append(p.createMultiBody(baseMass=1, baseCollisionShapeIndex=col_id, basePosition=startPos,
                                         baseOrientation=startOrientation))

    for i in range(steps):
        p.stepSimulation()

    updated_coords: list[SceneModelsCoords] = []

    for i in range(len(model_coords)):
        model_pos, model_rot = p.getBasePositionAndOrientation(box_ids[i])
        updated_coords.append(SceneModelsCoords(position=Transform3(x=model_pos[0], y=model_pos[2], z=model_pos[1]),
                                                rotation=Transform4(x=model_rot[0], y=model_rot[2], z=model_rot[1],
                                                                    w=model_rot[3]),
                                                scale=model_coords[i].scale))

    p.disconnect()

    return updated_coords
