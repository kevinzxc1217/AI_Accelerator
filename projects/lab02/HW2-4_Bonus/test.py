import onnx

onnx_model = onnx.load('./lenet.onnx')

# The model is represented as a protobuf structure and it can be accessed
# using the standard python-for-protobuf methods

## list all the operator types in the model
node_list = []
count = []
for i in onnx_model.graph.node:
    if (i.op_type not in node_list):
        node_list.append(i.op_type)
        count.append(1)
    else:
        idx = node_list.index(i.op_type)
        count[idx] = count[idx]+1
print(node_list)
print(count)

kernellst = []


onnx_model = onnx.load('./lenet.onnx')
for i in onnx_model.graph.node:
    if (i.op_type == 'Conv'):
        print(i)

# kp = []
# index = 0
# for i in onnx_model.graph.node:
#     if (i.op_type == 'Conv'):
#         kp.append([i.attribute[2].ints[0]])
#         if(len(i.attribute)>=4):
#             kp[index].append( i.attribute[3].ints[0])
#         else:
#             kp[index].append(0)
#         index = index+1
# print(kp)


# onnx_model = onnx.load('./lenet.onnx')
# for i in onnx_model.graph.node:
#     if (i.op_type == 'Conv'):
#         print(i)

# padlst = []
# for i in onnx_model.graph.node:
#     if (i.op_type == 'Conv'):
#         if(len(i.attribute)>=4):
#             padlst.append( i.attribute[3].ints)
# print(padlst)

# kp = []

