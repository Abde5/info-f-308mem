from scipy.io import wavfile
import matplotlib.pyplot as plt
import numpy as np

if __name__ == "__main__":
    samplerate, data = wavfile.read('sample.wav')
    data = data[:140]
    times = np.arange(len(data))/float(samplerate)
    timestretch = np.arange(len(data))/float(samplerate)*2

    plt.figure(1)

    subplt=plt.subplot(211)
    plt.fill_between(times, data[:,0], data[:,1], color='k')
    plt.xlim(times[0], times[139])
    plt.xlim([0,0.006])
    subplt.set_title("Signal d'origine ($samples$=140, $rate$=$1$)")


    subplt=plt.subplot(212)
    plt.fill_between(timestretch, data[:,0], data[:,1], color='k')
    plt.xlim(timestretch[0], timestretch[139])
    plt.xlim([0,0.006])
    subplt.set_title("Signal manipulé ($samples$=140, $rate$=$0.5$)")

    plt.tight_layout()
    
    plt.savefig('fig1.eps')
    
