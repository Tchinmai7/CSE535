package com.cse535.aslclassifier;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    private int classifyRow(String line) {
        String[] rowVals = line.split(",");
        float[] row = new float[rowVals.length];
        int i = 0;
        for (String r: rowVals) {
            row[i] = Float.parseFloat(r);
            i++;
        }
        if (row.length != 23) {
            return -1;
        }
        if (row[22] < 161.574) {
            if (row[16] < 173.2) {
                return 0;
            } else {
                if (row[16] < 206.57) {
                    if (row[20] < 210.304) {
                        return 0;
                    } else {
                        if (row[3] < 143.52) {
                            if (row[12] < 151.678) {
                                return 1;
                            } else {
                                return 0;
                            }
                        } else {
                            if (row[2] < 76.2507) {
                                return 0;
                            } else {
                                return 1;
                            }
                        }
                    }
                } else {
                    if (row[4] < 94.2055) {
                        if (row[22] < 129.823) {
                            if (row[5] < 120.139) {
                                if (row[18] < 264.748) {
                                    return 1;
                                } else {
                                    return 0;
                                }
                            } else {
                                if (row[11] < 175.657 ) {
                                    return 0;
                                } else {
                                    return 1;
                                }
                            }
                        } else {
                            if (row[17] < 28.7567) {
                                if (row[19] < 108.191) {
                                    return 1;
                                } else {
                                    return 0;
                                }
                            } else {
                                if (row[20] < 247.928) {
                                    return 0;
                                } else {
                                    return 1;
                                }

                            }
                        }
                    } else {
                        if (row[13] < 32.873) {
                            return 0;
                        } else {
                            if (row[16] < 207.639) {
                                if (row[18] < 185.649) {
                                    return 1;
                                } else {
                                    return 0;
                                }
                            } else {
                                return 1;
                            }
                        }
                    }
                }
            }
        } else {
            if (row[20] < 284.923) {
                if (row[10] < 176.012) {
                    if (row[9] < 109.793) {
                        if (row[20] < 133.575) {
                            if (row[2] < 115.03) {
                                return 0;
                            } else {
                                return 1;
                            }
                        } else {
                            if (row[7] < 121.728) {
                                if (row[1] < 115.098) {
                                    return 0;
                                } else {
                                    return 1;
                                }
                            } else {
                                if (row[13] < 100.587) {
                                    return 0;
                                } else {
                                    return 1;
                                }
                            }

                        }
                    } else {
                        if (row[15] < 177.02) {
                            if (row[6] < 97.052) {
                                return 0;
                            } else {
                                return 1;
                            }
                        } else {
                            if (row[2] < 71.5512) {
                                return 1;
                            } else {
                                if (row[7] < 137.581) {
                                    return 1;
                                } else {
                                    return 0;
                                }
                            }
                        }

                    }
                } else {
                    if (row[15] < 182.219) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            } else {
                if (row[13] < 69.5778) {
                    if (row[16] < 248.35) {
                        if (row[1] < 122.106) {
                            return 1;
                        } else {
                            return 0;
                        }
                    } else {
                        if (row[20] < 336.75) {
                            if (row[17] < 34.7111) {
                                if (row[18] < 208.121) {
                                    return 1;
                                } else {
                                    return 0;
                                }

                            } else {
                                if (row[12] < 195.568) {
                                    return 1;
                                } else {
                                    return 0;
                                }
                            }
                        } else {
                            if (row[13] < 57.5315) {
                                return 0;
                            } else {
                                if (row[3] < 130.191) {
                                    return 0;
                                } else {
                                    return 1;
                                }
                            }
                        }
                    }
                } else {
                    if (row[12] < 820.641) {
                        if (row[17] < 20.7043) {
                            if (row[18] < 259.669) {
                               if (row[20] < 334.645) {
                                   return 0;
                               } else {
                                   return 1;
                               }
                            } else {
                                if (row[20] < 307.44) {
                                    return 0;
                                } else {
                                    return 1;
                                }
                            }
                        } else {
                            if (row[13] < 76.1159) {
                                if (row[3] < 130.259) {
                                    return 1;
                                } else {
                                    return 0;
                                }
                            } else {
                                return 1;
                            }
                        }
                    } else {
                        return 0;
                    }
                }
            }
        }
    }
}
