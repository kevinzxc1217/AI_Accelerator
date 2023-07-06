import cv2  #used to convert BGR to RGB/gray_level.
import numpy as np  #2D/3D matrix
from matplotlib import pyplot as plt  #show photo that OpenCV reads or creates/ save photo




#definition and implementation of function convolution_2D
#------------------------------------------
def convolution_2D(image, kernel, padding=0, strides=1):

    ## compute the size of output image
    #/////////////////////////////////////////////////////
    xKernShape = kernel.shape[0] # = filter的width  #kernel.shape[0]回傳kernel的橫向總長
    yKernShape = kernel.shape[1] # = filter的height  #kernel.shape[1]回傳kernel的縱向總長
    xImgShape = image.shape[0] # = input的width  #image.shape[0]回傳image的橫向總長
    yImgShape = image.shape[1] # = input的height
    
    xOutput = int(((xImgShape - xKernShape + 2 * padding) / strides) + 1) # = output的width
    yOutput = int(((yImgShape - yKernShape + 2 * padding) / strides) + 1) # = output的height

    ## Then we can create a fresh matrix with the deduced dimensions.
    output = np.zeros((xOutput, yOutput))
    #/////////////////////////////////////////////////////

    #使input->input padding
    #/////////////////////////////////////////////////////
    if padding != 0:
        imagePadded = np.zeros((image.shape[0] + padding*2, image.shape[1] + padding*2))
        ## We then replace the inner portion of the padded image with the actual image.
        imagePadded[int(padding):int(-1 * padding), int(padding):int(-1 * padding)] = image
    else:
        ## If there is no padding
        imagePadded = image
    #/////////////////////////////////////////////////////
    
    

    ## do convolution. Iterate through image
    for y in range(imagePadded.shape[1]):
        if y > imagePadded.shape[1] - yKernShape:
            break
        # Only Convolve if y has gone down by the specified Strides
        if y % strides == 0:
            for x in range(imagePadded.shape[0]): 
                if x > imagePadded.shape[0] - xKernShape:
                    break
                try:
                    # Only Convolve if x has moved by the specified Strides
                    if x % strides == 0:
                        output[x, y] = (kernel * imagePadded[x : x + xKernShape,    y : y + yKernShape] ).sum()
                        #element-wise 乘法進行加權總合得output
                except:
                    break
    return output
#end of function convolution_2D
#------------------------------------------





#2D with multiple kernel channel
#input size(N, Cin, H, W) = (1, 4, 4, 5)
#output size(N, Cout, Hout, Wout) = (2, 5, 3, 3)
#output[N.i, Cout.j] = bias[Cout.j] + sigma(weight[Cout.j, k] * input[N.i, k])|k=0-Cin-1  #element-wise multiplicaton
  #因為是多channel 2D conv，所以是跳過m方向，沿著channel方向取點。
  #這樣取出的點可以視為一個個單通道2d conv的輸入資料
  #再把沿著channel方向取點的資料置入單通道的2D conv作運算。
  #因為單層單通道時，得到的output.shape=(H,W)
  #因此單層多通道時，得到的output.shape=(H,W)
  #因此多層多通道時，得到的output.shape=(M,H,W)
#------------------------------------------
def convolution3D(image, kernel, padding=0, strides=1):  
    M = kernel.shape[0] #2
    Kc = kernel.shape[1]  #3
    Kh = kernel.shape[2] #3
    Kw = kernel.shape[3] #3
    print("M:{}  Kc:{}  Kh:{}  Kw:{}".format(M, Kc, Kh, Kw))
    
    C = image.shape[2] #3 =b
    H = image.shape[0] #183 =r
    W = image.shape[1] #275 =g
    print("C:{}  H:{}  W:{}\n".format(C, H, W))

    image_part0 = np.array([image[:,:,0]])  #在rgb裡，這其實是生成R  

    for i in range(1, C): #因為C和Kc必須一樣，所以也可以用 for i in range(1, Kc):
        image_part0 = np.append(image_part0,  np.array([image[:,:,i]]),  axis=0) 
    image_along_channel = image_part0 
    output = np.zeros((M, H, W))
    for m in range(M):
        for c in range (C):
            output[m] += convolution_2D(image_along_channel[c], kernel[m][c], padding=1, strides=1) #記得這裡也要設padding=1
    return output
#END OF FUNC. convolution3D
#------------------------------------------


#driver program
#------------------------------------------
#原本設計的規格為
#input shape:(183, 275, 3) == (H,W,C)
#kernel shape:(2, 3, 3, 3)  == (M,Kc,Kh,Kw)
#convolution3D output.shape:(2, 183, 275)  == (D2, H2, W2)  , since H2 = 183-3+2+1 = 183, D2 = M




## The first testing kernel try to emphase edge in the Red color space
## The 2nd testing kernel try to get the grayscale conversion
## Gray = R*0.299 + G*0.587 + B*0.114
kernel = np.array([[[[-1, -1, -1], [-1, 20, -1], [-1, -1, -1]], 
                  [[-1, -1, -1], [-1, 0, -1], [-1, -1, -1]],
                  [[-1, -1, -1], [-1, 0, -1], [-1, -1, -1]]],
                   [[[0, 0, 0], [0, 0.299 , 0], [0, 0, 0]], 
                  [[0, 0, 0], [0, 0.587, 0], [0, 0, 0]],
                  [[0, 0, 0], [0, 0.114, 0], [0, 0, 0]]]])

img = cv2.imread("10.jpg") #FIXME: 改為你的圖片名稱
plt.imshow(img[...,::-1]) 
plt.show()
print("image shape :{}".format(img.shape))
print("kernel shape :{}".format(kernel.shape))

output = convolution3D(img, kernel, padding=1)
print("output shape :{}".format(output.shape))

plt.imshow(output[0,:,:])
plt.show()
plt.imshow(output[1,:,:],cmap='gray')
plt.show()