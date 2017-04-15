from scipy.io import wavfile
import matplotlib.pyplot as plt
import numpy as np

if __name__ == "__main__":
    samplerate, data = wavfile.read('deadmau5.wav')
    #data=data[:140]
    times = np.arange(len(data))/float(samplerate)
    timestretch = np.arange(len(data))/float(samplerate)*2

    plt.figure(1)

    subplt=plt.subplot(211)
    #plt.specgram(data.flatten(),Fs=samplerate,NFFT = 256)
    #plt.xlim([0,0.006])
    nfft = 256  # Length of the windowing segments
    fs = 256    # Sampling frequency
    pxx, freqs, bins, im = plt.specgram(data.flatten()[:10000], nfft,fs)

    subplt.set_title("Signal d'origine ($samples$=140, $rate$=$1$)")


    subplt=plt.subplot(212)
    plt.fill_between(timestretch, data[:,0], data[:,1], color='k')
    plt.xlim(timestretch[0], timestretch[139])
    plt.xlim([0,0.006])
    subplt.set_title("Signal manipulé ($samples$=140, $rate$=$0.5$)")

    plt.tight_layout()
    
    plt.savefig('fig2.png')
    
